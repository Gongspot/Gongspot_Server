package com.gongspot.project.domain.like.converter;

import com.gongspot.project.domain.like.dto.LikeResponseDTO;
import com.gongspot.project.domain.like.entity.Like;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.place.entity.Place;

import java.util.*;
import java.util.stream.Collectors;

public class LikeConverter {
    public static LikeResponseDTO.LikedPlaceDTO toLikedPlaceDTO(Like like, Double rating, Map<Long, Media> placeImgUrl){
        Place place = like.getPlace();
        Media placeMedia = placeImgUrl.get(place.getId());
        String imageUrl = (placeMedia != null) ? placeMedia.getUrl() : null;

        return LikeResponseDTO.LikedPlaceDTO.builder()
                .placeId(place.getId())
                .name(place.getName())
                .rating(rating)
                .hashtag(place.getType().name())
                .imageUrl(imageUrl)
                .isLiked(true)
                .isFree(place.getIsFree())
                .build();

    }

    public static LikeResponseDTO.LikedPlaceListDTO toLikedPlaceListDTO(List<Like> likes, Map<Long, Media> placeImgUrl , Map<Long, Double> placeRatings) {
        List<LikeResponseDTO.LikedPlaceDTO> likedPlaceDTOs = likes.stream()
                .map(like -> {
                    Double rating = placeRatings.getOrDefault(like.getPlace().getId(), 0.0);
                    return toLikedPlaceDTO(like, rating, placeImgUrl);
                })
                .collect(Collectors.toList());

        return LikeResponseDTO.LikedPlaceListDTO.builder()
                .totalCount(likedPlaceDTOs.size())
                .likedPlace(likedPlaceDTOs)
                .build();
    }
}

