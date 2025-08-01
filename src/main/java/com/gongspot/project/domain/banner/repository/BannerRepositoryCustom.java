package com.gongspot.project.domain.banner.repository;

import com.gongspot.project.domain.banner.dto.BannerResponseDTO;

import java.util.List;

public interface BannerRepositoryCustom {
    List<BannerResponseDTO.GetBannerDTO> findBanner();
}
