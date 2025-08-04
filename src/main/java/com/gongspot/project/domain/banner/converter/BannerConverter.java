package com.gongspot.project.domain.banner.converter;

import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.banner.entity.Banner;
import com.gongspot.project.domain.media.entity.Media;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BannerConverter {

    public static BannerResponseDTO.GetBannerDTO toBannerDTO(Banner banner, Optional<Media> thumbnailMediaOptional) {
        String thumbnailUrl = null;
        if (thumbnailMediaOptional.isPresent()) {
            thumbnailUrl = thumbnailMediaOptional.get().getUrl();
        }
        return BannerResponseDTO.GetBannerDTO.builder()
                .bannerId(banner.getId())
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

    public static BannerResponseDTO.GetBannerListDTO toBannerListDTO(List<BannerResponseDTO.GetBannerDTO> bannerDTOList) {
        return BannerResponseDTO.GetBannerListDTO.builder()
                .bannerList(bannerDTOList)
                .build();
    }

    public static BannerResponseDTO.GetBannerDetailDTO toBannerDetailDTO(Banner banner, Optional<Media> thumbnailMediaOptional, List<Media> attachments) {

        String thumbnailUrl = null;
        if (thumbnailMediaOptional.isPresent()) {
            thumbnailUrl = thumbnailMediaOptional.get().getUrl();
        }

        List<BannerResponseDTO.AttachmentDTO> attachmentDTOs = attachments.stream()
                .map(media -> BannerResponseDTO.AttachmentDTO.builder()
                        .attachmentId(media.getId())
                        .url(media.getUrl())
                        .fileName(media.getOriginalFileName())
                        .build())
                .collect(Collectors.toList());


        return new BannerResponseDTO.GetBannerDetailDTO(
                banner.getId(),
                banner.getTitle(),
                banner.getContent(),
                banner.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                thumbnailUrl,
                attachmentDTOs
        );
    }
}
