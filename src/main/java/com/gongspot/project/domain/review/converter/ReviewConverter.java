package com.gongspot.project.domain.review.converter;

import com.gongspot.project.common.enums.CongestionEnum;
import com.gongspot.project.common.enums.FacilitiesEnum;
import com.gongspot.project.common.enums.MoodEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.review.dto.ReviewRequestDTO;
import com.gongspot.project.domain.review.dto.ReviewResponseDTO;
import com.gongspot.project.domain.review.entity.Review;
import com.gongspot.project.domain.user.entity.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReviewConverter {

    public static ReviewResponseDTO.GetReviewDTO toGetReviewDTO(Review review, List<Media> reviewMediaList) {

        List<String> imageUrls = new ArrayList<>();
        if (reviewMediaList != null && !reviewMediaList.isEmpty()) {
            imageUrls = reviewMediaList.stream()
                    .map(Media::getUrl)
                    .collect(Collectors.toList());
        }

        return ReviewResponseDTO.GetReviewDTO.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .nickname(review.getUser().getNickname())
                .profileImageUrl(review.getUser().getProfileImg())
                .datetime(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                .rating(review.getRating())
                .reviewImageUrl(imageUrls)
                .content(review.getContent())
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

    public static ReviewResponseDTO.RatingPercentageDTO toRatingPercentageDTO(Map<Integer, Long> ratingCounts, long totalCount) {
        return ReviewResponseDTO.RatingPercentageDTO.builder()
                .fiveStarPercentage(calculatePercentage(ratingCounts.getOrDefault(5, 0L), totalCount))
                .fourStarPercentage(calculatePercentage(ratingCounts.getOrDefault(4, 0L), totalCount))
                .threeStarPercentage(calculatePercentage(ratingCounts.getOrDefault(3, 0L), totalCount))
                .twoStarPercentage(calculatePercentage(ratingCounts.getOrDefault(2, 0L), totalCount))
                .oneStarPercentage(calculatePercentage(ratingCounts.getOrDefault(1, 0L), totalCount))
                .build();
    }


    public static List<ReviewResponseDTO.CategoryCountDTO> toCategoryListDTO(List<Review> reviews) {
        Map<PurposeEnum, Long> purposeCounts = reviews.stream()
                .flatMap(review -> review.getPurpose().stream())
                .collect(Collectors.groupingBy(purpose -> purpose, Collectors.counting()));

        PurposeEnum topPurpose = purposeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Map<MoodEnum, Long> moodCounts = reviews.stream()
                .flatMap(review -> review.getMood().stream())
                .collect(Collectors.groupingBy(mood -> mood, Collectors.counting()));

        List<MoodEnum> topMoods = moodCounts.entrySet().stream()
                .sorted(Map.Entry.<MoodEnum, Long>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Map<FacilitiesEnum, Long> facilitiesCounts = reviews.stream()
                .flatMap(review -> review.getFacilities().stream())
                .collect(Collectors.groupingBy(facility -> facility, Collectors.counting()));

        FacilitiesEnum topFacility = facilitiesCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        List<ReviewResponseDTO.CategoryCountDTO> categoryList = new ArrayList<>();

        if (topPurpose != null) {
            categoryList.add(ReviewResponseDTO.CategoryCountDTO.builder()
                    .category(topPurpose.name())
                    .count(purposeCounts.get(topPurpose).intValue())
                    .build());
        }

        for (MoodEnum mood : topMoods) {
            categoryList.add(ReviewResponseDTO.CategoryCountDTO.builder()
                    .category(mood.name())
                    .count(moodCounts.get(mood).intValue())
                    .build());
        }

        if (topFacility != null) {
            categoryList.add(ReviewResponseDTO.CategoryCountDTO.builder()
                    .category(topFacility.name())
                    .count(facilitiesCounts.get(topFacility).intValue())
                    .build());
        }

        return categoryList;
    }

    public static ReviewResponseDTO.GetReviewListDTO toGetReviewListDTO(
            List<Review> reviews,
            List<Media> mediaList,
            Double averageRating,
            List<ReviewResponseDTO.CategoryCountDTO> categoryList,
            Map<Integer, Long> ratingCounts,
            int totalReviewCount
    ) {

        Map<Long, List<Media>> reviewMediaMap = mediaList.stream()
                .collect(Collectors.groupingBy(media -> media.getReview().getId()));


        List<ReviewResponseDTO.GetReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> toGetReviewDTO(review, reviewMediaMap.getOrDefault(review.getId(), new ArrayList<>())))
                .collect(Collectors.toList());

        return ReviewResponseDTO.GetReviewListDTO.builder()
                .reviewCount(totalReviewCount)
                .averageRating(averageRating)
                .ratingPercentages(toRatingPercentageDTO(ratingCounts, totalReviewCount))
                .categoryList(categoryList)
                .reviewList(reviewDTOs)
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

    private static Double calculatePercentage(Long count, long total) {
        if (total == 0) return 0.0;
        return (count * 100.0) / total;
    }

    public static Review toReview(User user, Place place, ReviewRequestDTO.ReviewRegisterDTO reqDTO) {
        return Review.builder()
                .user(user)
                .place(place)
                .datetime(reqDTO.getDatetime())
                .rating(reqDTO.getRate())
                .congestion(reqDTO.getCongest())
                .purpose(reqDTO.getPurpose())
                .mood(reqDTO.getMood())
                .facilities(reqDTO.getFacility())
                .content(reqDTO.getContent())
                .build();
    }
}