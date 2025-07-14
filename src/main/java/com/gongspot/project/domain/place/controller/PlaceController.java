package com.gongspot.project.domain.place.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.service.GooglePlaceDetailService;
import com.gongspot.project.domain.place.service.PlaceCommandService;
import com.gongspot.project.domain.place.service.PlaceQueryService;
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
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final GooglePlaceDetailService googlePlaceDetailService;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "공간 상세조회")
    @GetMapping("/{placeId}")
    public ApiResponse<PlaceResponseDTO.GetPlaceDTO> getPlace(@PathVariable Long placeId){

        Long userId = authenticatedUserUtils.getAuthenticatedUserId();

        PlaceResponseDTO.GetPlaceDTO result = placeQueryService.getPlace(userId,placeId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "키워드 기반 장소 상세 검색", description = "키워드로 첫 번째 장소를 검색하고 상세 정보를 가져옵니다.")
    @GetMapping("/api/places/details")
    public ApiResponse<PlaceResponseDTO.GetPlaceResponseDTO> getPlaceDetails(
            @RequestParam String keyword
    ) {
        PlaceResponseDTO.GetPlaceResponseDTO response = googlePlaceDetailService.searchPlaceDetail(keyword);
        return ApiResponse.onSuccess(response);
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
}