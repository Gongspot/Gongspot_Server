package com.gongspot.project.domain.banner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BannerResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class GetBannerDTO {
        Long bannerId;
        String thumbnailUrl;
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
        Long bannerId;
        String title;
        String content;
        String datetime;
        String thumbnailUrl;
        List<AttachmentDTO> attachments;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttachmentDTO {
        private Long attachmentId;
        private String url;
        private String fileName;
    }
}