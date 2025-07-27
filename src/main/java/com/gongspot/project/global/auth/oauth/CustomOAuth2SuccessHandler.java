package com.gongspot.project.global.auth.oauth;

import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.service.UserService;
import com.gongspot.project.global.auth.JwtTokenProvider;
import com.gongspot.project.global.auth.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final TokenService tokenService;

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, UserService userService, TokenService tokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        var oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();

        // kakao_account map 가져오기
        var kakaoAccount = (java.util.Map<String, Object>) oauth2User.getAttribute("kakao_account");
        String email = null;
        String nickname = "기본닉네임";
        String profileImage = "기본이미지URL";

        if (kakaoAccount != null) {
            email = (String) kakaoAccount.get("email");

            var profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                nickname = (String) profile.get("nickname");
                profileImage = (String) profile.get("profile_image_url");
            }
        }

        // email 없으면 다른 식별자로 처리 (예: id)
        User user = userService.findByEmail(email);
        if (user == null) {
            user = userService.createUser(email, nickname, profileImage);
        }

        TokenService.TokenPair tokenPair = tokenService.generateAndSaveTokens(user);
        String accessToken = tokenPair.accessToken();
        String refreshToken = tokenPair.refreshToken();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("{\"accessToken\": \"" + accessToken + "\", " +
                "\"refreshToken\": \"" + refreshToken + "\"}");

    }
}
