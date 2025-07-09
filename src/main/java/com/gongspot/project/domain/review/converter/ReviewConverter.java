package com.gongspot.project.domain.review.converter;

import com.gongspot.project.common.enums.FacilitiesEnum;
import com.gongspot.project.common.enums.MoodEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.review.dto.ReviewResponseDTO;
import com.gongspot.project.domain.review.entity.Review;

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
            Map<Integer, Long> ratingCounts) {

        Map<Long, List<Media>> reviewMediaMap = mediaList.stream()
                .collect(Collectors.groupingBy(media -> media.getReview().getId()));


        List<ReviewResponseDTO.GetReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> toGetReviewDTO(review, reviewMediaMap.getOrDefault(review.getId(), new ArrayList<>())))
                .collect(Collectors.toList());

        return ReviewResponseDTO.GetReviewListDTO.builder()
                .reviewCount(reviews.size())
                .averageRating(averageRating)
                .ratingPercentages(toRatingPercentageDTO(ratingCounts, reviews.size()))
                .categoryList(categoryList)
                .reviewList(reviewDTOs)
                .build();
    }

    private static Double calculatePercentage(Long count, long total) {
        if (total == 0) return 0.0;
        return (count * 100.0) / total;
    }
}
