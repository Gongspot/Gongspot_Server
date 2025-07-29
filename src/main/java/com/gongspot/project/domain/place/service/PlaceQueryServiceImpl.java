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
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceQueryServiceImpl implements PlaceQueryService{
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

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
    public List<PlaceResponseDTO.SearchPlaceDTO> getFilteredPlaces(Long userId, String keyword, List<PurposeEnum> purpose, PlaceEnum type, List<MoodEnum> mood, List<FacilitiesEnum> facilities, List<LocationEnum> location, Long page) {
        return placeRepository.findFilteredPlaces(userId, keyword, purpose, type, mood, facilities, location, page);
    }
}