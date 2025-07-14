package com.gongspot.project.domain.user.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.dto.UserResponseDTO;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void registerNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (nickname == null) {
            throw new GeneralException(ErrorStatus.NICKNAME_NOT_EXIST);
        }

        user.updateNickname(nickname);
    }

    @Override
    @Transactional
    public UserResponseDTO.ProfileResponseDTO updateProfile(Long userId, UserRequestDTO.ProfileRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        user.updateProfile(request.getNickname(), request.getProfileImg());

        if (!user.getNickname().equals(request.getNickname())
                && userRepository.existsByNickname(request.getNickname())) {
            throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
        }

        return UserResponseDTO.ProfileResponseDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
    }

}
