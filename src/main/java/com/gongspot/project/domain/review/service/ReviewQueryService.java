package com.gongspot.project.domain.review.service;

import com.gongspot.project.domain.review.dto.ReviewResponseDTO;

public interface ReviewQueryService {
    ReviewResponseDTO.GetReviewListDTO getReviewList(Long placeId, int page);
}
