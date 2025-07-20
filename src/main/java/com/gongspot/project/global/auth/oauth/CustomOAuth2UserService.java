package com.gongspot.project.global.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

            Object idObj = attributes.get("id");
            if (idObj != null) {
                attributes.put("id", String.valueOf(idObj));
            }

            // attributes 구조 확인
            System.out.println("Kakao attributes: " + attributes);

            String userNameAttributeName = "id";

            return new DefaultOAuth2User(
                    Collections.singleton(() -> "ROLE_USER"),
                    attributes,
                    userNameAttributeName
            );
        } catch (OAuth2AuthenticationException e) {
            // 로그 찍기..
            System.out.println("OAuth2AuthenticationException 발생 >>> " + e.getError().getErrorCode());
            System.out.println("OAuth2AuthenticationException 상세 메시지 >>> " + e.getError().getDescription());
            throw e;
        }
    }

}
