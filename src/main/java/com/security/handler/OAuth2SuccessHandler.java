package com.security.handler;

import com.security.entities.UserEntity;
import com.security.services.JWTService;
import com.security.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JWTService jwtService;

    @Value("${app.env}")
    private String appEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        logger.info(email);

        UserEntity user = userService.getUserByEmail(email);

        if(user == null){
            UserEntity newUser = UserEntity.builder()
                    .name(oAuth2User.getName())
                    .email(email)
                    .build();
            user = userService.saveUser(newUser);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Adding cookie to client browser...
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        // Adding security and http-only access
        cookie.setHttpOnly(true);
        // Security is set only when we running in production mode
        cookie.setSecure("production".equals(appEnv));
        response.addCookie(cookie);

        String frontendUrl = "http://localhost:8080/home.html?token=" + accessToken;
        response.sendRedirect(frontendUrl);
    }
}
