package com.gongspot.project.domain.like.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.like.converter.LikeConverter;
import com.gongspot.project.domain.like.dto.LikeResponseDTO;
import com.gongspot.project.domain.like.entity.Like;
import com.gongspot.project.domain.like.repository.LikeRepository;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.review.repository.ReviewRepository;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeQueryServiceImpl implements LikeQueryService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public LikeResponseDTO.LikedPlaceListDTO getLikedPlaces(Long userId, String isFree) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Like> likedLikes;
        switch (isFree.toUpperCase()) {
            case "FREE":
                likedLikes = likeRepository.findAllByUserAndPlaceIsFreeTrue(user);
                break;
            case "PAID":
                likedLikes = likeRepository.findAllByUserAndPlaceIsFreeFalse(user);
                break;
            case "ALL":
            default:
                likedLikes = likeRepository.findAllByUser(user);
                break;
        }

        List<Place> likedPlaces = likedLikes.stream()
                .map(Like::getPlace)
                .collect(Collectors.toList());

        if (likedPlaces.isEmpty()){
            return LikeResponseDTO.LikedPlaceListDTO.builder()
                    .totalCount(0)
                    .likedPlace(new ArrayList<>())
                    .build();
        }
        Map<Long, Double> placeRatings = new HashMap<>();
        for (Place place : likedPlaces) {
            Double avgRating = reviewRepository.getAverageRatingByPlaceId(place.getId());
            placeRatings.put(place.getId(), avgRating != null ? avgRating : 0.0);
        }

        return LikeConverter.toLikedPlaceListDTO(likedLikes, placeRatings);
    }
}
