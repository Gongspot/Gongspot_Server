package com.gongspot.project.global.auth.dto;

public class TokenResponseDTO {
    private final String accessToken;

    public TokenResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
