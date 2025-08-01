package com.gongspot.project.global.config;

import com.gongspot.project.global.auth.*;
import com.gongspot.project.global.auth.filter.JwtAuthenticationFilter;
import com.gongspot.project.global.auth.oauth.CustomAccessDeniedHandler;
import com.gongspot.project.global.auth.oauth.CustomOAuth2SuccessHandler;
import com.gongspot.project.global.auth.service.CustomOAuth2UserService;
import com.gongspot.project.global.auth.service.TokenBlacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
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
            "/auth/logout",
            "/auth/oauth/kakao/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2SuccessHandler customOAuth2SuccessHandler,
                                           CorsConfigurationSource corsConfigurationSource,
                                           JwtTokenProvider jwtTokenProvider, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomOAuth2UserService customOAuth2UserService,
                                           TokenBlacklistService tokenBlacklistService, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Swagger 설정
                                .requestMatchers(allowedUrls).permitAll()  // 허용 URL 설정
                                .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customOAuth2SuccessHandler)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 안된 경우
                        // 리디렉션 없이 에러코드 출력하도록 변경
                        .accessDeniedHandler(customAccessDeniedHandler) // 권한 모자란 경우
                );



        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenBlacklistService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
