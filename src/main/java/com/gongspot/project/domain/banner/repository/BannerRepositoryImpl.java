package com.gongspot.project.domain.banner.repository;

import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.banner.entity.QBanner;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QBanner banner = QBanner.banner;

    @Override
    public List<BannerResponseDTO.GetBannerDTO> findBanner() {
        return queryFactory
                .select(Projections.constructor(BannerResponseDTO.GetBannerDTO.class,
                        banner.id,
                        banner.imgUrl
                ))
                .from(banner)
                .fetch();
    }
}
