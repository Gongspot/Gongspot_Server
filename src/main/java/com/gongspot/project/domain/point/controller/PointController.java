package com.gongspot.project.domain.point.controller;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.point.dto.PointHistoryDTO;
import com.gongspot.project.domain.point.dto.PointResponseDTO;
import com.gongspot.project.domain.point.service.PointQueryService;
import com.gongspot.project.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@Validated
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
@Tag(name = "포인트", description = "포인트 관련 API")
public class PointController {

    private final PointQueryService pointQueryService;

    @Operation(summary = "현재 포인트 조회 API", description = "로그인된 사용자의 현재 포인트를 조회합니다.")
    @GetMapping("/total")
    public ApiResponse<PointResponseDTO> getTotalPoints() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        PointResponseDTO result = pointQueryService.getTotalPoints(userId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "포인트 내역 조회 API", description = "로그인된 사용자의 포인트 적립/사용 내역을 페이징으로 조회합니다.")
    @GetMapping("/history")
    public ApiResponse<PageResponse<PointHistoryDTO>> getPointHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        PageResponse<PointHistoryDTO> response = pointQueryService.getPointHistory(userId, pageable);

        return ApiResponse.onSuccess(response);
    }
}