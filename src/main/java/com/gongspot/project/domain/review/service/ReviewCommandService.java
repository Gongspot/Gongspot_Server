package com.gongspot.project.domain.review.service;

import com.gongspot.project.domain.review.dto.ReviewRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewCommandService {
    void saveReview(Long userId, Long placeId, ReviewRequestDTO.ReviewRegisterDTO reqDTO, List<MultipartFile> reviewPictures);
}
