package com.gongspot.project.domain.place.converter;

import com.gongspot.project.common.enums.CongestionEnum;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.review.entity.Review;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PlaceConverter {

    public static PlaceResponseDTO.GetPlaceDTO toGetPlaceDTO(Place place, Double rating, List<Review> congestionList ,Boolean isLiked) {

        List<PlaceResponseDTO.CongestionDTO> congestionDTOS = congestionList.stream()
                .map(review -> PlaceResponseDTO.CongestionDTO.builder()
                        .reviewId(review.getId())
                        .userId(review.getUser().getId())
                        .nickname(review.getUser().getNickname())
                        .profileImageUrl(review.getUser().getProfileImg())
                        .congestion(mapCongestionToString(review.getCongestion()))
                        .daytime(toRelativeDaytime(review.getDatetime()))
                        .datetime(toRelativeDateString(review.getDatetime()))
                        .build())
                .toList();

        return PlaceResponseDTO.GetPlaceDTO.builder()
                .placeId(place.getId())
                .name(place.getName())
                .isFree(place.getIsFree())
                .isLiked(isLiked)
                .rating(rating)
                .hashtag(place.getType().name())
                .locationInfo(place.getLocationInfo())
                .photoUrl(place.getPhotoUrl())
                .openingHours(place.getOpeningHours())
                .phoneNumber(place.getPhoneNumber())
                .congestionList(congestionDTOS)
                .build();
    }
    private static String toRelativeDaytime(LocalDateTime dateTime) {
        long days = Duration.between(dateTime.toLocalDate().atStartOfDay(), LocalDateTime.now().toLocalDate().atStartOfDay()).toDays();

        if (days == 0) return "오늘";
        else if (days > 0 && days < 4) return days + "일 전";
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");
            return dateTime.format(formatter);
        }
    }

    private static String toRelativeDateString(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(dateTime,now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 2) {
            return "1시간 전";
        } else if (hours < 3) {
            return "2시간 전";
        } else if (hours < 4) {
            return "3시간 전";
        }else {
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
    }

    private static String mapCongestionToString(CongestionEnum congestion) {
        if (congestion == null) {
            return null;
        }
        switch (congestion) {
            case 높음:
                return "혼잡도 높음 '거의 만석이에요'";
            case 보통:
                return "혼잡도 보통 '자리 남아있어요'";
            case 낮음:
                return "혼잡도 낮음 '자리 여유있어요'";
            default:
                return congestion.name();
        }
    }

    public Place convertToPlaceEntity(PlaceResponseDTO.PlaceApprovalRequestDTO request) {
        PlaceResponseDTO.GooglePlaceDTO dto = request.getGooglePlace();

        Place place = new Place();
        place.setName(dto.getName());
        place.setPhotoUrl(dto.getPhotoUrl());
        place.setLocationInfo(dto.getFormattedAddress());
        place.setOpeningHours(dto.getOpeningHours());
        place.setPhoneNumber(dto.getInternationalPhoneNumber());

        // 관리자 입력 필드
        place.setPurpose(request.getPurpose());
        place.setType(request.getType());
        place.setMood(request.getMood());
        place.setFacilities(request.getFacilities());
        place.setLocation(request.getLocation());
        place.setIsFree(request.getIsFree());

        return place;
    }

    private String buildInformation(PlaceResponseDTO.GooglePlaceDTO dto) {
        return String.format(
                "주소: %s\n전화: %s\n운영시간: %s",
                dto.getFormattedAddress(),
                dto.getInternationalPhoneNumber(),
                dto.getOpeningHours()
        );
    }

}

