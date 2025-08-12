package com.gongspot.project.domain.banner.service;

import com.gongspot.project.domain.banner.dto.BannerRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerCommandService {
    void updateBanner(Long bannerId, BannerRequestDTO bannerRequestDTO, MultipartFile thumbnailFile, List<Long> mediaIdsToDelete, List<MultipartFile> attachments);
    void deleteBanner(Long bannerId);
}
