package com.gongspot.project.domain.review.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.review.dto.ReviewRequestDTO;
import com.gongspot.project.domain.review.service.ReviewCommandService;
import com.gongspot.project.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Tag(name = "Review")
@Validated
public class ReviewController {

    private final ReviewCommandService reviewCommandService;

    @PostMapping("/{placeId}")
    @Operation(summary = "공간 리뷰 등록", description = "공간 리뷰를 등록합니다.")
    public ApiResponse<Void> createReview(
            @PathVariable("placeId") Long placeId,
            @Valid @RequestBody ReviewRequestDTO.ReviewRegisterDTO reqDTO) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        reviewCommandService.saveReview(userId, placeId, reqDTO);
        return ApiResponse.onSuccess();
    }
}
