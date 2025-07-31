package com.gongspot.project.domain.home.repository;

import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.domain.home.dto.HomeResponseDTO;
import com.gongspot.project.domain.home.entity.QHotCheck;
import com.gongspot.project.domain.place.entity.QPlace;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.gongspot.project.domain.like.entity.QLike.like;
import static com.gongspot.project.domain.review.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class HomeRepositoryImpl implements HomeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HomeResponseDTO.HotPlaceDTO> findTop10PlacesByWeeklyVisits(Long userId) {
        QPlace place = QPlace.place;
        QHotCheck hotCheck = QHotCheck.hotCheck;

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        JPQLQuery<Long> likedSubquery = JPAExpressions
                .select(like.id)
                .from(like)
                .where(like.place.eq(place)
                        .and(like.user.id.eq(userId)));

        return queryFactory
                .select(Projections.constructor(HomeResponseDTO.HotPlaceDTO.class,
                        place.id,
                        place.name,
                        place.photoUrl,
                        hotCheck.cnt.sum().as("totalVisits"),
                        likedSubquery.exists()
                ))
                .from(hotCheck)
                .join(hotCheck.place, place)
                .where(hotCheck.updatedAt.goe(oneWeekAgo))
                .groupBy(place.id, place.name, place.photoUrl)
                .orderBy(hotCheck.cnt.sum().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<HomeResponseDTO.CategoryPlaceDTO> findRandomPlacesExcluding(Long userId, PlaceEnum placeType, List<Long> excludeIds) {
        QPlace place = QPlace.place;

        BooleanExpression categoryCondition = place.type.eq(placeType);
        BooleanExpression excludeCondition = (excludeIds != null && !excludeIds.isEmpty()) ? place.id.notIn(excludeIds) : null;

        JPQLQuery<Long> likedSubquery = JPAExpressions
                .select(like.id)
                .from(like)
                .where(like.place.eq(place)
                        .and(like.user.id.eq(userId)));

        JPQLQuery<Double> avgRatingSubquery = JPAExpressions
                .select(review.rating.avg())
                .from(review)
                .where(review.place.eq(place));

        return queryFactory
                .select(Projections.constructor(HomeResponseDTO.CategoryPlaceDTO.class,
                        place.id,
                        place.name,
                        avgRatingSubquery,
                        place.locationInfo,
                        place.photoUrl,
                        likedSubquery.exists()))
                .from(place)
                .where(categoryCondition, excludeCondition)
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(20)
                .fetch();
    }
}
