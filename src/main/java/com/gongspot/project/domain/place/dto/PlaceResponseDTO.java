package com.gongspot.project.domain.place.dto;

import com.gongspot.project.common.enums.CongestionEnum;
import com.gongspot.project.common.enums.PlaceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PlaceResponseDTO {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CongestionDTO {
        Long reviewId;
        Long userId;
        String nickname;
        String profileImageUrl;
        CongestionEnum congestion;
        String daytime;
        String datetime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPlaceDTO {
        Long placeId;
        String name;
        Boolean isFree;
        Boolean isLiked;
        Double rating;
        List<PlaceEnum> hashtags;
        String information;
        List<CongestionDTO> congestionList;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetPlaceResponseDTO {
        private String placeId;
        private String name;
        private String url;
        private double rating;
        private String photoUrl;
    }
}
