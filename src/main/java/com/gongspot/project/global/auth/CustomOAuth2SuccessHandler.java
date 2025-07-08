package com.gongspot.project.global.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        var oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();

        // kakao_account map 가져오기
        var kakaoAccount = (java.util.Map<String, Object>) oauth2User.getAttribute("kakao_account");
        String email = null;

        if (kakaoAccount != null) {
            email = (String) kakaoAccount.get("email");
        }

        // email 없으면 다른 식별자로 처리 (예: id)
        if (email == null) {
            email = String.valueOf(oauth2User.getAttribute("id"));
        }

        // JWT 발급
        String token = jwtTokenProvider.createToken(email);

        // JSON 형태로 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("{\"access token\": \"" + token + "\"}");
    }
}
