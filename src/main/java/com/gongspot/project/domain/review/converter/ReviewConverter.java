package com.gongspot.project.domain.review.converter;

import com.gongspot.project.common.enums.CongestionEnum;
import com.gongspot.project.domain.review.dto.ReviewResponseDTO;
import com.gongspot.project.domain.review.entity.Review;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReviewConverter {

    public static ReviewResponseDTO.CongestionListDTO congestionListDTO(List<Review> reviews){
        Map<LocalDate,List<Review>> reviewByDate = reviews.stream()
                .collect(Collectors.groupingBy(review -> review.getDatetime().toLocalDate()));

        List<ReviewResponseDTO.DateCongestionListDTO> dateCongestionList = new ArrayList<>();

        reviewByDate.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<Review>>comparingByKey().reversed())
                .forEach(entry -> {
                    String formattedDate = toDateString(entry.getKey());
                    List<ReviewResponseDTO.CongestionItemDTO> congestionItems = entry.getValue().stream()
                            .map(ReviewConverter::toCongestionItemDTO)
                            .collect(Collectors.toList());

                    ReviewResponseDTO.DateCongestionListDTO dateCongestion = ReviewResponseDTO.DateCongestionListDTO.builder()
                            .date(formattedDate)
                            .dateCongestionList(congestionItems)
                            .build();

                    dateCongestionList.add(dateCongestion);
                });
        return ReviewResponseDTO.CongestionListDTO.builder()
                .congestionList(dateCongestionList)
                .build();

    }

    private static ReviewResponseDTO.CongestionItemDTO toCongestionItemDTO(Review review) {
        return ReviewResponseDTO.CongestionItemDTO.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .nickname(review.getUser().getNickname())
                .profileImageUrl(review.getUser().getProfileImg())
                .congestion(mapCongestionToString(review.getCongestion()))
                .daytime(toRelativeDaytime(review.getDatetime()))
                .datetime(toRelativeDateString(review.getDatetime()))
                .build();
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
    private static String toDateString(LocalDate dateTime) {
        String dayOfWeek;
        switch (dateTime.getDayOfWeek()) {
            case MONDAY: dayOfWeek = "월"; break;
            case TUESDAY: dayOfWeek = "화"; break;
            case WEDNESDAY: dayOfWeek = "수"; break;
            case THURSDAY: dayOfWeek = "목"; break;
            case FRIDAY: dayOfWeek = "금"; break;
            case SATURDAY: dayOfWeek = "토"; break;
            case SUNDAY: dayOfWeek = "일"; break;
            default: dayOfWeek = ""; break;
        }

        return String.format("%d.%d.%d.%s",
                dateTime.getYear() % 100,
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(),
                dayOfWeek);
    }
}
