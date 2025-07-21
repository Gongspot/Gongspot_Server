package com.gongspot.project.domain.like.dto;

import lombok.*;
import java.util.List;

public class LikeResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedPlaceDTO {
        Long placeId;
        String name;
        Double rating;
        String hashtag;
        String imageUrl;
        Boolean isLiked;
        Boolean isFree;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedPlaceListDTO {
        Integer totalCount;
        List<LikedPlaceDTO> likedPlace;
    }
}
