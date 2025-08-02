package com.gongspot.project.domain.banner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BannerResponseDTO {
    @Getter
    @AllArgsConstructor
    public static class GetBannerDTO {
        Long BannerId;
        String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetBannerListDTO {
        List<GetBannerDTO> bannerList;
    }

    @Getter
    @AllArgsConstructor
    public static class GetBannerDetailDTO {
        Long BannerId;
        String title;
        String content;
        String imageUrl;
        String datetime;
    }
}
