package com.gongspot.project.domain.place.converter;

import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.review.entity.Review;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlaceConverter {

    public static PlaceResponseDTO.GetPlaceDTO toGetPlaceDTO(Place place, Double rating, List<Review> congestionList ) {

        List<PlaceResponseDTO.CongestionDTO> congestionDTOS = congestionList.stream()
                .map(review -> PlaceResponseDTO.CongestionDTO.builder()
                        .reviewId(review.getId())
                        .userId(review.getUser().getId())
                        .nickname(review.getUser().getNickname())
                        .profileImageUrl(review.getUser().getProfileImg())
                        .congestion(review.getCongestion())
                        .daytime(toRelativeDaytime(review.getDatetime()))
                        .datetime(toRelativeDateString(review.getDatetime()))
                        .build())
                .toList();

        return PlaceResponseDTO.GetPlaceDTO.builder()
                .placeid(place.getId())
                .name(place.getName())
                .isfree(place.getIsFree())
                .rating(rating)
                .hashtags(place.getType())
                .information(place.getInformation())
                .congestionList(congestionDTOS)
                .build();
    }
    private static String toRelativeDaytime(LocalDateTime dateTime) {
        long days = Duration.between(dateTime.toLocalDate().atStartOfDay(), LocalDateTime.now().toLocalDate().atStartOfDay()).toDays();

        if (days == 0) return "오늘";
        else return days + "일 전";
    }

    private static String toRelativeDateString(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(dateTime, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 60) {
            return -minutes + "분 전";
        } else if (hours < 2) {
            return "1시간 전";
        } else {
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
    }
}
