package com.gongspot.project.domain.place.dto;

import com.gongspot.project.common.enums.*;
import lombok.*;

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
        String hashtag;
        String photoUrl;
        String locationInfo;
        String openingHours;
        String phoneNumber;

        List<CongestionDTO> congestionList;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GooglePlaceDTO {
        private String placeId;
        private String name;
        private String formattedAddress;
        private String internationalPhoneNumber;
        private String geometry; // 좌표 문자열
        private String openingHours; // 요일별 문자열
        private String secondaryOpeningHours;
        private String photoUrl;
    }

    @Getter
    @Setter
    public static class PlaceApprovalRequestDTO {

        // 구글 맵 기반 자동 추출 정보
        private GooglePlaceDTO googlePlace;

        // 관리자 수동 세팅 값
        private List<PurposeEnum> purpose;
        private PlaceEnum type;
        private List<MoodEnum> mood;
        private List<FacilitiesEnum> facilities;
        private List<LocationEnum> location;
        private Boolean isFree;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchPlaceListDTO {
        List<SearchPlaceDTO> placeList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchPlaceDTO {
        Long placeId;
        String name;
        Double rating;
        PlaceEnum hashtag;
        String imageUrl;
        Boolean isLike;
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
