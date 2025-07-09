package com.gongspot.project.domain.review.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import com.gongspot.project.domain.review.converter.ReviewConverter;
import com.gongspot.project.domain.review.dto.ReviewResponseDTO;
import com.gongspot.project.domain.review.entity.Review;
import com.gongspot.project.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService{
    private final ReviewRepository reviewRepository;
    private final MediaRepository mediaRepository;
    private final PlaceRepository placeRepository;

    @Override
    public ReviewResponseDTO.GetReviewListDTO getReviewList(Long placeId, int page) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        List<Review> allReviews = reviewRepository.findAllByPlace(place);

        List<Review> pagedReviews = reviewRepository.findAllByPlaceOrderByCreatedAtDesc(
                place, PageRequest.of(page, 15)).getContent();

        List<Media> mediaList = mediaRepository.findAllByReviewIn(allReviews);

        Double averageRating = allReviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        Map<Integer, Long> ratingCounts = allReviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        List<ReviewResponseDTO.CategoryCountDTO> categoryList = ReviewConverter.toCategoryListDTO(allReviews);

        return ReviewConverter.toGetReviewListDTO(
                pagedReviews,
                mediaList,
                averageRating,
                categoryList,
                ratingCounts
        );
    }

}
