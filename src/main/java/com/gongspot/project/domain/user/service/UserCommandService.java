package com.gongspot.project.domain.user.service;

public interface UserCommandService {
    void registerNickname(Long userId, String nickname);
}
