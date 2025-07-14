package com.gongspot.project.domain.home.repository;

import com.gongspot.project.domain.home.dto.HomeResponseDTO;
import com.gongspot.project.domain.home.entity.QHotCheck;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.entity.QPlace;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HomeRepositoryImpl implements HomeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HomeResponseDTO.HotPlaceDTO> findTop10PlacesByWeeklyVisits() {
        QPlace place = QPlace.place;
        QHotCheck hotCheck = QHotCheck.hotCheck;

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        return queryFactory
                .select(Projections.constructor(HomeResponseDTO.HotPlaceDTO.class,
                        place.id,
                        place.name,
                        hotCheck.cnt.sum().as("totalVisits")))
                .from(hotCheck)
                .join(hotCheck.place, place)
                .where(hotCheck.updatedAt.goe(oneWeekAgo))
                .groupBy(place.id, place.name)
                .orderBy(hotCheck.cnt.sum().desc())
                .limit(10)
                .fetch();
    }
}
