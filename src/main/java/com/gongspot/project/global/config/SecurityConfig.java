package com.gongspot.project.global.config;

import com.gongspot.project.global.auth.*;
import com.gongspot.project.global.auth.filter.JwtAuthenticationFilter;
import com.gongspot.project.global.auth.oauth.CustomOAuth2SuccessHandler;
import com.gongspot.project.global.auth.oauth.CustomOAuth2UserService;
import com.gongspot.project.global.auth.service.TokenBlacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String[] allowedUrls = {
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v1/posts/**",
            "/api/v1/replies/**",
            "/auth/login",
            "/auth/login/kakao/**",
            "/auth/logout"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2SuccessHandler customOAuth2SuccessHandler, JwtTokenProvider jwtTokenProvider, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomOAuth2UserService customOAuth2UserService, TokenBlacklistService tokenBlacklistService) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(allowedUrls).permitAll()  // 허용 URL 설정
                                .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customOAuth2SuccessHandler)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        // 리디렉션 없이 에러코드 출력하도록 변경
                );



        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenBlacklistService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
