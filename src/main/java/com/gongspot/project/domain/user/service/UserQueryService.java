package com.gongspot.project.domain.user.service;

import com.gongspot.project.domain.user.dto.UserResponseDTO;

public interface UserQueryService {

    UserResponseDTO.ProfileViewResponseDTO getProfile(Long userId);
}