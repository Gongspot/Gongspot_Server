package com.gongspot.project.domain.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.review.dto.ReviewRequestDTO;
import com.gongspot.project.domain.review.service.ReviewCommandService;
import com.gongspot.project.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Tag(name = "Review")
@Validated
public class ReviewController {

    private final ReviewCommandService reviewCommandService;

    @PostMapping(value = "/{placeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "공간 리뷰 등록", description = "공간 리뷰를 등록합니다.")
    public ApiResponse<Void> createReview(
            @PathVariable("placeId") Long placeId,
            @RequestPart("review") @Valid ReviewRequestDTO.ReviewRegisterDTO review,
            @RequestPart(name = "reviewPictures", required = false) List<MultipartFile> reviewPictures) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        reviewCommandService.saveReview(userId, placeId, review, reviewPictures);
        return ApiResponse.onSuccess();
    }
}
