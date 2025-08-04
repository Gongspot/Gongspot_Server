package com.gongspot.project.domain.banner.repository;

import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.banner.entity.QBanner;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.stringTemplate;

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

    @Override
    public BannerResponseDTO.GetBannerDetailDTO findBannerDetailById(Long bannerId) {
        return queryFactory
                .select(Projections.constructor(
                        BannerResponseDTO.GetBannerDetailDTO.class,
                        banner.id,
                        banner.title,
                        banner.content,
                        banner.imgUrl,
                        stringTemplate("DATE_FORMAT({0}, '%Y.%m.%d')", banner.createdAt)
                ))
                .from(banner)
                .where(banner.id.eq(bannerId))
                .fetchOne();
    }
}
