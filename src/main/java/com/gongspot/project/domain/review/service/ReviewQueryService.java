package com.gongspot.project.domain.review.service;

import com.gongspot.project.domain.review.dto.ReviewResponseDTO;

public interface ReviewQueryService {
    ReviewResponseDTO.CongestionListDTO getCongestionList(Long userId, Long placeId, int page);
    ReviewResponseDTO.GetReviewListDTO getReviewList(Long placeId, int page);
}