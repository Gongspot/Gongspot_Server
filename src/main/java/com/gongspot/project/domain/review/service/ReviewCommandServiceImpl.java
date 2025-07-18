package com.gongspot.project.domain.review.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.home.service.HomeCommandService;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import com.gongspot.project.domain.review.converter.ReviewConverter;
import com.gongspot.project.domain.review.dto.ReviewRequestDTO;
import com.gongspot.project.domain.review.entity.Review;
import com.gongspot.project.domain.review.repository.ReviewRepository;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewCommandServiceImpl implements ReviewCommandService {

    public final PlaceRepository placeRepository;
    public final UserRepository userRepository;
    public final ReviewRepository reviewRepository;
    public final HomeCommandService homeCommandService;

    @Override
    public void saveReview(Long userId, Long placeId, ReviewRequestDTO.ReviewRegisterDTO reqDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        Review newReview = ReviewConverter.toReview(user, place, reqDTO);

        int week = reqDTO.getDatetime().getDayOfWeek().getValue() - 1;

        try {
            reviewRepository.save(newReview);
            homeCommandService.updateHotCheck(place, week);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorStatus.REVIEW_SAVE_FAIL);
        }
    }
}
