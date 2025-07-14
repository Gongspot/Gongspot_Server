package com.gongspot.project.domain.user.converter;

import com.gongspot.project.domain.user.dto.UserResponseDTO;
import com.gongspot.project.domain.user.entity.User;

public class UserConverter {

    public static UserResponseDTO.ProfileResponseDTO toProfileResponseDTO(User user) {
        return UserResponseDTO.ProfileResponseDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
    }

}
