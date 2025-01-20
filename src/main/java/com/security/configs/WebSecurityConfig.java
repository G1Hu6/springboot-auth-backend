package com.security.configs;

import com.security.filters.JwtAuthFilter;
import com.security.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.security.entities.enums.Permission.*;
import static com.security.entities.enums.Role.ADMIN;
import static com.security.entities.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private static final String[] PUBLIC_ROUTES = {"/error","/auth/**","/home.html"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        return httpSecurity
                //.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/new_login.html"))
                .authorizeHttpRequests(auth -> auth
                                //.anyRequest().authenticated()
                                 .requestMatchers(PUBLIC_ROUTES).permitAll()
                                //.requestMatchers("/post/**").authenticated()
                                 .requestMatchers(HttpMethod.GET ,"/posts/**").permitAll()
                                 .requestMatchers(HttpMethod.POST,"/posts/**").hasAnyRole(CREATOR.name(), USER_CREATE.name(), POST_CREATE.name())
                                 .requestMatchers(HttpMethod.PUT,"/posts/**").hasAnyRole(CREATOR.name(), USER_UPDATE.name(), POST_CREATE.name())
                                 .requestMatchers(HttpMethod.DELETE,"/posts/**").hasAnyRole(CREATOR.name(), USER_DELETE.name(), POST_DELETE.name())
                                 //.requestMatchers(HttpMethod.POST ,"/posts/**").hasAnyRole(ADMIN.name(), CREATOR.name())
                                 //.requestMatchers("/posts/**").hasAnyRole("ADMIN")
                                 //.anyRequest().authenticated()
                                //.requestMatchers("/posts/**").permitAll()
                                //.requestMatchers("/posts").permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfig-> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .oauth2Login(
//                        oauth2login-> oauth2login
//                                .failureUrl("/login?error=true")
//                                .successHandler(oAuth2SuccessHandler)
//                )
                .build();

    }

//    @Bean
//    UserDetailsService muInMemoryUserDetailsService(){
//        UserDetails normalUser = User.builder()
//                .username("pranav")
//                .password(passwordEncoder().encode("1234"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
