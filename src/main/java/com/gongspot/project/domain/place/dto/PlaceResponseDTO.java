package com.gongspot.project.domain.place.dto;

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
        String congestion;
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
        String hashtag;
        String information;
        List<CongestionDTO> congestionList;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetPlaceResponseDTO {
        private String placeId;
        private String name;
        private String formattedAddress;
        private String internationalPhoneNumber;
        private String geometry; // 좌표 문자열
        private String openingHours; // 요일별 문자열
        private String secondaryOpeningHours;
        private String photoUrl;
    }
}
