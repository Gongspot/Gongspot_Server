package com.gongspot.project.domain.banner.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.banner.converter.BannerConverter;
import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.banner.service.BannerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banners")
@Tag(name = "이벤트 배너", description = "이벤트 배너 관련 API")
public class BannerController {

    private final BannerQueryService bannerQueryService;

    @GetMapping()
    @Operation(summary = "이벤트 배너 조회", description = "이벤트 배너를 조회합니다.")
    public ApiResponse<BannerResponseDTO.GetBannerListDTO> getBannerList() {
        List<BannerResponseDTO.GetBannerDTO> bannerList = bannerQueryService.getBanner();
        return ApiResponse.onSuccess(BannerConverter.toBannerListDTO(bannerList));
    }

    @GetMapping("/{banners_id}")
    @Operation(summary = "이벤트 배너 상세 조회", description = "이벤트 배너를 상세 조회합니다.")
    public ApiResponse<BannerResponseDTO.GetBannerDetailDTO> getBannerDetailList(
            @PathVariable("banners_id") Long banners_id
    ) {
        BannerResponseDTO.GetBannerDetailDTO bannerDetail = bannerQueryService.getBannerDetail(banners_id);
        return ApiResponse.onSuccess(bannerDetail);
    }
}
