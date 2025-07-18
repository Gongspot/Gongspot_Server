package com.gongspot.project.domain.user.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.dto.UserResponseDTO;
import com.gongspot.project.domain.user.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserCommandService userCommandService;

    // 닉네임 등록
    @PostMapping("/nickname")
    public ApiResponse<UserResponseDTO.NicknameResponseDTO> registerNickname(
            @RequestBody UserRequestDTO.NicknameRequestDTO request
    ) {
        Long userId = getCurrentUserId();
        userCommandService.registerNickname(userId, request.getNickname());
        return ApiResponse.onSuccess(null);
    }

    // 프로필 수정(사진, 닉네임)
    @PatchMapping("/profile")
    public ApiResponse<UserResponseDTO.ProfileResponseDTO> updateProfile(@RequestBody UserRequestDTO.ProfileRequestDTO request) {
        Long userId = getCurrentUserId();
        UserResponseDTO.ProfileResponseDTO response = userCommandService.updateProfile(userId, request);
        return ApiResponse.onSuccess(response);
    }

    // userId 변환
    private Long getCurrentUserId() {
        return 1L;
    }


}
