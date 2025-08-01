package com.gongspot.project.domain.banner.service;

import com.gongspot.project.domain.banner.dto.BannerResponseDTO;

import java.util.List;

public interface BannerQueryService {
    List<BannerResponseDTO.GetBannerDTO> getBanner();
}
