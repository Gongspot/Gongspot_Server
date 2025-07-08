package com.gongspot.project.domain.place.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.service.GooglePlaceDetailService;
import com.gongspot.project.domain.place.service.PlaceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@Tag(name = "Place")
@Validated
public class PlaceController {

    private final PlaceQueryService placeQueryService;
    private final GooglePlaceDetailService googlePlaceDetailService;

    @Operation(summary = "공간 상세조회")
    @GetMapping("/{placeId}")
    public ApiResponse<PlaceResponseDTO.GetPlaceDTO> getPlace(@PathVariable Long placeId){

        PlaceResponseDTO.GetPlaceDTO result = placeQueryService.getPlace(placeId);

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

}
