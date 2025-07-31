package com.gongspot.project.domain.review.service;

import com.gongspot.project.domain.review.dto.ReviewRequestDTO;

public interface ReviewCommandService {
    void saveReview(Long userId, Long placeId, ReviewRequestDTO.ReviewRegisterDTO reqDTO);
}
