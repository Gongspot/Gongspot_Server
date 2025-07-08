package com.gongspot.project.domain.user.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/nickname")
    public ApiResponse<void> registerNickname(@RequestBody UserRequestDTO.NicknameRequestDTO request) {
        Long userId = getCurrentUserId();
        userCommandService.registerNickname(userId, request.getNickname());
        return ApiResponse.onSuccess(null);
    }

    private Long getCurrentUserId() {
        return 1L;
    }
}
