package com.gongspot.project.domain.user.service;

import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.dto.UserResponseDTO;
import com.gongspot.project.domain.user.entity.User;

public interface UserCommandService {

    void registerNickname(Long userId, String nickname);
    void quitService(Long userId);
    UserResponseDTO.ProfileResponseDTO updateProfile(Long userId, UserRequestDTO.ProfileRequestDTO request);
    User updateAllPreferences(Long userId, UserRequestDTO.PreferRequestDTO request);
}
