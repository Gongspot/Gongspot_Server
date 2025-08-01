package com.gongspot.project.domain.banner.service;

import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerQueryServiceImpl implements BannerQueryService {
    private final BannerRepository bannerRepository;
    @Override
    public List<BannerResponseDTO.GetBannerDTO> getBanner() {
        return bannerRepository.findBanner();
    }
}
