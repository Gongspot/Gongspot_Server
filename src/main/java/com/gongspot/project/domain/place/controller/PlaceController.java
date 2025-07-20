package com.gongspot.project.domain.place.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.like.dto.LikeResponseDTO;
import com.gongspot.project.domain.like.service.LikeQueryService;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.service.PlaceCommandService;
import com.gongspot.project.domain.place.service.PlaceQueryService;
import com.gongspot.project.domain.review.dto.ReviewResponseDTO;
import com.gongspot.project.domain.review.service.ReviewQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.gongspot.project.global.auth.AuthenticatedUserUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
@Tag(name = "Place")
@Validated
public class PlaceController {

    private final PlaceQueryService placeQueryService;
    private final PlaceCommandService placeCommandService;
    private final ReviewQueryService reviewQueryService;
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final LikeQueryService likeQueryService;

    @Operation(summary = "공간 상세조회")
    @GetMapping("/{placeId}")
    public ApiResponse<PlaceResponseDTO.GetPlaceDTO> getPlace(@PathVariable Long placeId){

        Long userId = authenticatedUserUtils.getAuthenticatedUserId();

        PlaceResponseDTO.GetPlaceDTO result = placeQueryService.getPlace(userId,placeId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "공간 리뷰목록 조회", description = "특정 공간의 리뷰 목록을 20개씩 페이징하여 조회합니다.")
    @GetMapping("/{placeId}/reviews")
    public ApiResponse<ReviewResponseDTO.GetReviewListDTO> getReviewList(
            @PathVariable Long placeId,
            @RequestParam(defaultValue = "0") int page) {

        ReviewResponseDTO.GetReviewListDTO result = reviewQueryService.getReviewList(placeId, page);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "공간 찜하기")
    @PostMapping("/{placeId}/isLiked")
    public ApiResponse<String> likedPlace(@PathVariable Long placeId) {

        Long userId = authenticatedUserUtils.getAuthenticatedUserId();

        placeCommandService.isLikedPlace(userId,placeId);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "공간 찜 취소")
    @DeleteMapping("/{placeId}/isLiked")
    public ApiResponse<String> unLikedPlace(@PathVariable Long placeId) {

        Long userId = authenticatedUserUtils.getAuthenticatedUserId();

        placeCommandService.unLikedPlace(userId,placeId);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "내가 찜한 공간 목록 조회", description = "전체(ALL)/무료(FREE)/유료(PAID) 필터링")
    @GetMapping("/liked")
    public ApiResponse<LikeResponseDTO.LikedPlaceListDTO> getLikedPlaces(
            @RequestParam(name = "isFree", defaultValue = "ALL") String isFree) {

        Long userId = authenticatedUserUtils.getAuthenticatedUserId();
        LikeResponseDTO.LikedPlaceListDTO result = likeQueryService.getLikedPlaces(userId, isFree);
        return ApiResponse.onSuccess(result);
    }
}