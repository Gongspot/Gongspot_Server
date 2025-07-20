package com.gongspot.project.domain.home.repository;

import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.domain.home.dto.HomeResponseDTO;
import com.gongspot.project.domain.home.entity.QHotCheck;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.entity.QPlace;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Override
    public List<HomeResponseDTO.CategoryPlaceDTO> findRandomPlacesExcluding(PlaceEnum placeType, List<Long> excludeIds) {
        QPlace place = QPlace.place;

        BooleanExpression categoryCondition = place.type.any().eq(placeType);
        BooleanExpression excludeCondition = (excludeIds != null && !excludeIds.isEmpty()) ? place.id.notIn(excludeIds) : null;

        return queryFactory
                .select(Projections.constructor(HomeResponseDTO.CategoryPlaceDTO.class,
                        place.id,
                        place.name))
                .from(place)
                .where(categoryCondition, excludeCondition)
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(20)
                .fetch();
    }
}
