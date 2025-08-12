package com.gongspot.project.domain.review.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.notification.dto.NotificationRequestDTO;
import com.gongspot.project.domain.review.dto.ReviewRequestDTO;
import com.gongspot.project.domain.review.service.ReviewCommandService;
import com.gongspot.project.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @Parameter(
                    description = "리뷰 JSON 본문",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestPart("review") @Valid ReviewRequestDTO.ReviewRegisterDTO reqDTO,
            @Parameter(description = "첨부파일 리스트 (선택)")
            @RequestPart("reviewPictures") List<MultipartFile> reviewPictures) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        reviewCommandService.saveReview(userId, placeId, reqDTO, reviewPictures);
        return ApiResponse.onSuccess();
    }
}
