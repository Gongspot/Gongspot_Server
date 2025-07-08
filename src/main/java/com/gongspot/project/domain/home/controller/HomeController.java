package com.gongspot.project.domain.home.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.home.converter.HomeConverter;
import com.gongspot.project.domain.home.dto.HomeResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.home.service.HomeQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "Home")
@Validated
public class HomeController {

    private final HomeQueryService homeQueryService;

    @GetMapping("/hot")
    @Operation(summary = "Hot한 학습 공간 조회", description = "일주일 동안 리뷰가 많이 달린 순서로 10개 조회합니다.")
    public ApiResponse<HomeResponseDTO.HotPlaceListDTO> getHotPlace(){
        List<HomeResponseDTO.HotPlaceDTO> placeList = homeQueryService.getHotPlaceList();
        return ApiResponse.onSuccess(HomeConverter.homeHotPlaceListDTO(placeList));
    }
}
