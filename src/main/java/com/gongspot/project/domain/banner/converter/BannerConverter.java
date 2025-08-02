package com.gongspot.project.domain.banner.converter;

import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.home.dto.HomeResponseDTO;

import java.util.List;

public class BannerConverter {
    public static BannerResponseDTO.GetBannerListDTO toBannerListDTO(List<BannerResponseDTO.GetBannerDTO> banner) {
        return BannerResponseDTO.GetBannerListDTO.builder()
                .bannerList(banner)
                .build();
    }
}
