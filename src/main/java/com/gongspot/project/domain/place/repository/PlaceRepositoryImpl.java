package com.gongspot.project.domain.place.repository;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.enums.*;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.place.dto.PlaceRequestDTO;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.entity.QPlace;
import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.gongspot.project.domain.like.entity.QLike.like;
import static com.gongspot.project.domain.place.entity.QPlace.place;
import static com.gongspot.project.domain.review.entity.QReview.review;
import static java.lang.management.ThreadInfo.from;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceResponseDTO.SearchPlaceDTO> findFilteredPlaces(Long userId, String keyword, List<PurposeEnum> purpose, PlaceEnum type, List<MoodEnum> mood, List<FacilitiesEnum> facilities, List<LocationEnum> location, Long page) {
        BooleanBuilder builder = new BooleanBuilder();

        String normalizedKeyword = keyword.toLowerCase().replace(" ", "");

        if (keyword != null && !keyword.isBlank()) {
            builder.and(
                    Expressions.stringTemplate(
                            "REPLACE(LOWER({0}), ' ', '')", place.name
                    ).like("%" + normalizedKeyword + "%")
            );
        } else {
            BooleanBuilder filterGroup = new BooleanBuilder();

            if (purpose != null && !purpose.isEmpty()) {
                filterGroup.or(place.purpose.any().in(purpose));
            }

            if (type != null) {
                filterGroup.or(place.type.in(type));
            }

            if (mood != null && !mood.isEmpty()) {
                filterGroup.or(place.mood.any().in(mood));
            }

            if (facilities != null && !facilities.isEmpty()) {
                filterGroup.or(place.facilities.any().in(facilities));
            }

            if (location != null && !location.isEmpty()) {
                filterGroup.or(place.location.any().in(location));
            }

            builder.and(filterGroup);
        }

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
                .select(Projections.constructor(PlaceResponseDTO.SearchPlaceDTO.class,
                        place.id,
                        place.name,
                        avgRatingSubquery,
                        place.type,
                        place.photoUrl,
                        likedSubquery.exists(),
                        place.locationInfo
                ))
                .from(place)
                        .where(builder)
                        .offset(page * 10)
                        .limit(10)
                        .fetch();

    }
}
