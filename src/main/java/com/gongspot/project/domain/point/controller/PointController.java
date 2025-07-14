package com.gongspot.project.domain.point.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.point.dto.PointResponseDTO;
import com.gongspot.project.domain.point.service.PointQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {

    private final PointQueryService pointQueryService;

    @GetMapping("/total")
    public ApiResponse<PointResponseDTO> getTotalPoints() {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        PointResponseDTO result = pointQueryService.getTotalPoints(userId);

        return ApiResponse.onSuccess(result);
    }
}