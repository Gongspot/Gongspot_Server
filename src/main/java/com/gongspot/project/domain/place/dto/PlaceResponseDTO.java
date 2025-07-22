package com.gongspot.project.domain.place.dto;

import com.gongspot.project.common.enums.PlaceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
        List<PlaceEnum> hashtags;
        String information;
        List<CongestionDTO> congestionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VisitedPlaceDTO {
        private Long placeId;
        private String name;
        private Double rate;
        private LocalDate visitedDate;
        private String type; // enum 값의 name()
        private Boolean isLiked;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VisitedPlaceListDTO {
        private Integer totalCount;
        private List<VisitedPlaceDTO> visitedPlaces;
    }
}
