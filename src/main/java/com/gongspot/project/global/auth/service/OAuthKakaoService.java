package com.gongspot.project.global.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthKakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, String.class);

        try {
            Map<String, Object> tokenResponse = objectMapper.readValue(response.getBody(), Map.class);
            return (String) tokenResponse.get("access_token");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new GeneralException(ErrorStatus.KAKAO_TOKEN_REQUEST_FAILED);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.KAKAO_TOKEN_PARSE_FAILED);
        }
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(response.getBody(), Map.class);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.KAKAO_TOKEN_PARSE_FAILED);
        }
    }
}
