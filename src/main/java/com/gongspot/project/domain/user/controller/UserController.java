package com.gongspot.project.domain.user.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.dto.UserResponseDTO;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.service.UserCommandService;
import com.gongspot.project.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "프로필 관리", description = "초기 회원가입 이후 & 프로필 수정 API")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    // 닉네임 등록
    @Operation(summary = "닉네임 등록 API", description = "사용자의 닉네임을 초기 등록합니다. (카카오톡 닉네임을 기본값으로 가져옵니다.)")
    @PostMapping("/nickname")
    public ApiResponse<UserResponseDTO.NicknameResponseDTO> registerNickname(
            @RequestBody UserRequestDTO.NicknameRequestDTO request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userCommandService.registerNickname(user.getId(), request.getNickname());
        return ApiResponse.onSuccess(null);
    }

    // 프로필 수정(사진, 닉네임)
    @Operation(summary = "프로필 수정(사진, 닉네임) API", description = "사진, 닉네임 등에 해당하는 프로필을 수정합니다. (기본값 카카오 프로필)")
    @PatchMapping("/profile")
    public ApiResponse<UserResponseDTO.ProfileResponseDTO> updateProfile(
            @RequestBody UserRequestDTO.ProfileRequestDTO request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserResponseDTO.ProfileResponseDTO response = userCommandService.updateProfile(user.getId(), request);
        return ApiResponse.onSuccess(response);
    }

    // 프로필 조회
    @Operation(summary = "프로필 조회 API", description = "사용자의 프로필(닉네임, 프로필 사진)을 조회합니다.")
    @PostMapping("/profile/view")
    public ApiResponse<UserResponseDTO.ProfileViewResponseDTO> getProfile(
            @RequestBody UserRequestDTO.ProfileViewRequestDTO request
    ) {
        UserResponseDTO.ProfileViewResponseDTO response = userQueryService.getProfile(request.getUserId());
        return ApiResponse.onSuccess(response);
    }
}
