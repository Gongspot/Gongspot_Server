package com.gongspot.project.domain.place.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.like.entity.Like;
import com.gongspot.project.domain.like.repository.LikeRepository;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.point.entity.Point;
import com.gongspot.project.domain.point.repository.PointRepository;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceCommandServiceImpl implements PlaceCommandService{
    private final LikeRepository likeRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    @Override
    @Transactional
    public void isLikedPlace(Long userId,Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        Optional<Like> isLiked = likeRepository.findByUserAndPlace(user, place);

        if(isLiked.isPresent()){
            likeRepository.delete(isLiked.get());
        } else {
            Like like = Like.builder()
                    .user(user)
                    .place(place)
                    .build();
            likeRepository.save(like);
        }
    }

    @Override
    @Transactional
    public void unLikedPlace(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));
        Optional<Like> existingLike = likeRepository.findByUserAndPlace(user, place);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            throw new BusinessException(ErrorStatus.LIKE_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void viewCongestion(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        LocalDate today = LocalDate.now();
        if (pointRepository.existsByUserIdAndPlaceIdAndContentAndDate(
                userId, placeId, "혼잡도 확인", today)) {
            throw new BusinessException(ErrorStatus.ALREADY_VIEWED_TODAY);
        }

        Long currentPoints = pointRepository.getTotalPointsByUserId(userId);
        if (currentPoints < 2) {
            throw new BusinessException(ErrorStatus.INSUFFICIENT_POINTS);
        }

        Point point = new Point();
        point.setUser(user);
        point.setPlace(place);
        point.setUpdatedPoint(-2);
        point.setDate(LocalDate.now());
        point.setContent("혼잡도 확인");

        pointRepository.save(point);

    }
}