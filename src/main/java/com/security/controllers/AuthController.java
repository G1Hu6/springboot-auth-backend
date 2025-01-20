package com.security.controllers;

import com.security.dto.LogInDto;
import com.security.dto.LoginResponseDto;
import com.security.dto.SignUpDto;
import com.security.dto.UserDto;
import com.security.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${app.env}")
    private String appEnv;

    @PostMapping(path = "/signUp")
    public ResponseEntity<UserDto> signUpUser(@RequestBody SignUpDto signUpDto){
        return ResponseEntity.ok(authService.signUpUser(signUpDto));
    }

    @PostMapping(path = "/logIn")
    public ResponseEntity<LoginResponseDto> logInUser(@RequestBody LogInDto logInDto, HttpServletResponse response){
        LoginResponseDto loginResponse = authService.logInUser(logInDto);

        // Adding cookie to client browser...
        Cookie cookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        // Adding security and http-only access
        cookie.setHttpOnly(true);
        // Security is set only when we running in production mode
        cookie.setSecure("production".equals(appEnv));
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(
                        () -> new AuthenticationServiceException("Refresh token is not found inside the cookies")
                );
        LoginResponseDto loginResponseDto = authService.refresh(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }


}
