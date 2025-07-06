package com.gongspot.project.domain.place.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.service.PlaceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@Tag(name = "Place")
@Validated
public class PlaceController {

    private final PlaceQueryService placeQueryService;

    @Operation(summary = "공간 상세조회")
    @GetMapping("/{placeid}")
    public ApiResponse<PlaceResponseDTO.GetPlaceDTO> getPlace(@PathVariable Long placeid){

        PlaceResponseDTO.GetPlaceDTO result = placeQueryService.getPlace(placeid);

        return ApiResponse.onSuccess(result);
    }
}
