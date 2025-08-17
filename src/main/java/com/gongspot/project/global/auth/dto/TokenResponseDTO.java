package com.gongspot.project.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponseDTO {
    private final Long userId;
    private final boolean isAdmin;
    private final boolean isNewUser;
    private final String accessToken;
    private final String refreshToken;

    public TokenResponseDTO(Long userId, boolean isAdmin, boolean isNewUser, String accessToken, String refreshToken) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.isNewUser = isNewUser;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    @JsonProperty("isAdmin")
    public boolean isAdmin() {
        return isAdmin;
    }

    @JsonProperty("isNewUser")
    public boolean isNewUser() {
        return isNewUser;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
