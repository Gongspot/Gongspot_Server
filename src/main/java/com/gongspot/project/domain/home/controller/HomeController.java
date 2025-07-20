package com.gongspot.project.domain.home.controller;

import com.gongspot.project.common.exception.handler.BaseHandler;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gongspot.project.common.code.status.ErrorStatus.PLACE_CATEGORY_NOT_FOUND;

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

    @GetMapping("/category")
    @Operation(summary = "카테고리별 학습 공간 상세조회(목록)", description = "카테고리별로 랜덤으로 20개씩 조회합니다.")
    public ApiResponse<HomeResponseDTO.CategoryPlaceListDTO> getCategoryPlace(
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(name = "excludeIds", required = false) String excludeIds){

        if (categoryId < 1 || categoryId > 5) {
            throw new BaseHandler(PLACE_CATEGORY_NOT_FOUND);
        }

        List<Long> excludeIdsList;

        if (excludeIds != null && !excludeIds.isEmpty()) {
            excludeIdsList = HomeConverter.toExcludeIdList(excludeIds);
        } else {
            excludeIdsList = List.of();
        }

        List<HomeResponseDTO.CategoryPlaceDTO> placeList = homeQueryService.getCategoryPlaceList(categoryId, excludeIdsList);
        return ApiResponse.onSuccess(HomeConverter.homeCategoryPlaceListDTO(placeList));
    }
}
