package com.gongspot.project.domain.banner.service;

import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.banner.converter.BannerConverter;
import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.banner.entity.Banner;
import com.gongspot.project.domain.banner.repository.BannerRepository;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.media.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gongspot.project.common.code.status.ErrorStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerQueryServiceImpl implements BannerQueryService {
    private final BannerRepository bannerRepository;
    private final MediaRepository mediaRepository;

    @Override
    public BannerResponseDTO.GetBannerListDTO getBannersList() {

        List<Banner> bannerList = bannerRepository.findAllByOrderByCreatedAtDesc();
        List<BannerResponseDTO.GetBannerDTO> bannerDTOList = bannerList.stream()
                .map(banner -> {
                    Optional<Media> thumbnailMediaOptional = mediaRepository.findByBannerIdAndIsThumbnailTrue(banner.getId());

                    return BannerConverter.toBannerDTO(banner, thumbnailMediaOptional);
                })
                .collect(Collectors.toList());
        return BannerConverter.toBannerListDTO(bannerDTOList);
    }

    @Override
    public BannerResponseDTO.GetBannerDetailDTO getBannerDetail(Long bannerId) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.BANNER_NOT_FOUND));

        Optional<Media> thumbnailMediaOptional = mediaRepository.findByBannerIdAndIsThumbnailTrue(bannerId);

        List<Media> attachments = mediaRepository.findByBannerIdAndIsThumbnailFalse(bannerId);

        return BannerConverter.toBannerDetailDTO(banner,thumbnailMediaOptional, attachments);

    }
}
