package com.gongspot.project.domain.place.dto;

import com.gongspot.project.common.enums.*;
import lombok.*;

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

        private GooglePlaceDTO googlePlace;

        // 관리자 수동 세팅 값
        private List<PurposeEnum> purpose;
        private PlaceEnum type;
        private List<MoodEnum> mood;
        private List<FacilitiesEnum> facilities;
        private List<LocationEnum> location;
        private Boolean isFree;
    }

}
