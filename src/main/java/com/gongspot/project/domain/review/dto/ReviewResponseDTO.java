package com.gongspot.project.domain.review.dto;

import lombok.*;

import java.util.List;

public class ReviewResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewDTO {
        Long reviewId;
        Long userId;
        String nickname;
        String profileImageUrl;
        String datetime;
        Integer rating;
        List<String> reviewImageUrl;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CongestionItemDTO {
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
    public static class DateCongestionListDTO {
        private String date;
        private List<CongestionItemDTO> dateCongestionList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CongestionListDTO {
        private List<DateCongestionListDTO> congestionList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCountDTO {
        private String category;
        private Integer count;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingPercentageDTO {
        Double fiveStarPercentage;
        Double fourStarPercentage;
        Double threeStarPercentage;
        Double twoStarPercentage;
        Double oneStarPercentage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewListDTO {
        Integer reviewCount;
        Double averageRating;
        RatingPercentageDTO ratingPercentages;
        List<CategoryCountDTO> categoryList;
        List<GetReviewDTO> reviewList;
    }

}
