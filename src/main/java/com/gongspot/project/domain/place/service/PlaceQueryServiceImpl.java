package com.gongspot.project.domain.place.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.enums.*;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.like.repository.LikeRepository;
import com.gongspot.project.domain.place.converter.PlaceConverter;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import com.gongspot.project.domain.review.entity.Review;
import com.gongspot.project.domain.review.repository.ReviewRepository;
import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.search.repository.RecentSearchRepository;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gongspot.project.domain.place.entity.QPlace.place;

@Service
@RequiredArgsConstructor
public class PlaceQueryServiceImpl implements PlaceQueryService{
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final RecentSearchRepository recentSearchRepository;

    @Override
    public PlaceResponseDTO.GetPlaceDTO getPlace(Long userId, Long placeId){
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        Double averageRating = reviewRepository.getAverageRatingByPlaceId(placeId);
        List<Review> congestionList = reviewRepository.findTop3ByPlaceId(placeId, PageRequest.of(0, 3));

        boolean isLiked = false;
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));
            isLiked = likeRepository.existsByUserAndPlace(user, place);
        }
        return PlaceConverter.toGetPlaceDTO(place, averageRating, congestionList, isLiked);
    }

    @Override
    public PlaceResponseDTO.VisitedPlaceListDTO getVisitedPlaces(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByUser(user);

        List<PlaceResponseDTO.VisitedPlaceDTO> dtos = reviews.stream()
                .sorted(Comparator.comparing(Review::getDatetime).reversed()) // 최신순 정렬
                .map(review -> {
                    Place place = review.getPlace();
                    // 해당 공간의 전체 평균 별점 계산
                    Double avgRating = reviewRepository.getAverageRatingByPlaceId(place.getId());
                    boolean isLiked = likeRepository.existsByUserAndPlace(user, place);

                    return PlaceConverter.toVisitedPlaceDTO(
                            place,
                            review.getDatetime().toLocalDate(), // 각 리뷰의 실제 방문일
                            avgRating,
                            isLiked
                    );
                })
                .collect(Collectors.toList());

        return PlaceConverter.toVisitedPlaceListDTO(dtos);
    }

    @Override
    @Transactional
    public List<PlaceResponseDTO.SearchPlaceDTO> getFilteredPlaces(Long userId, String keyword, List<PurposeEnum> purpose, PlaceEnum type, List<MoodEnum> mood, List<FacilitiesEnum> facilities, List<LocationEnum> location, Long page) {
        if (keyword != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));

            RecentSearch recentSearch = RecentSearch.builder()
                    .user(user)
                    .keyword(keyword)
                    .build();

            recentSearchRepository.save(recentSearch);
        }
        return placeRepository.findFilteredPlaces(userId, keyword, purpose, type, mood, facilities, location, page);
    }

    @Override
    public Place getPlaceDetails(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));
    }
}