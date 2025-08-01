package com.gongspot.project.domain.user.service;

import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.dto.UserResponseDTO;

public interface UserCommandService {

    void registerNickname(Long userId, String nickname);

    UserResponseDTO.ProfileResponseDTO updateProfile(Long userId, UserRequestDTO.ProfileRequestDTO request);
}
