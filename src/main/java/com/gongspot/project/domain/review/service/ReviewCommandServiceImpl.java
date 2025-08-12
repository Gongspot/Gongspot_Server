package com.gongspot.project.domain.review.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.home.service.HomeCommandService;
import com.gongspot.project.domain.like.entity.Like;
import com.gongspot.project.domain.like.repository.LikeRepository;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import com.gongspot.project.domain.review.converter.ReviewConverter;
import com.gongspot.project.domain.review.dto.ReviewRequestDTO;
import com.gongspot.project.domain.review.entity.Review;
import com.gongspot.project.domain.review.repository.ReviewRepository;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import com.gongspot.project.domain.uuid.entity.Uuid;
import com.gongspot.project.domain.uuid.repository.UuidRepository;
import com.gongspot.project.global.aws.s3.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final LikeRepository likeRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final HomeCommandService homeCommandService;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final MediaRepository mediaRepository;

    @Override
    public void saveReview(Long userId, Long placeId, ReviewRequestDTO.ReviewRegisterDTO reqDTO, List<MultipartFile> reviewPictures) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        Review newReview = ReviewConverter.toReview(user, place, reqDTO);

        int week = reqDTO.getDatetime().getDayOfWeek().getValue() - 1;

        Optional<Like> existing = likeRepository.findByUserAndPlace(user, place);

        try {
            reviewRepository.save(newReview);
            homeCommandService.updateHotCheck(place, week);

            if (reqDTO.getLike() && existing.isEmpty()) {
                Like newLike = Like.builder()
                        .user(user)
                        .place(place)
                        .build();

                likeRepository.save(newLike);
            }

            for (MultipartFile picture : reviewPictures) {
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder()
                        .uuid(uuid).build());

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(picture.getSize());
                metadata.setContentType(picture.getContentType());

                String pictureUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid), savedUuid.getUuid(), picture, metadata);

                mediaRepository.save(ReviewConverter.toReviewImage(pictureUrl, newReview));
            }
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorStatus.REVIEW_SAVE_FAIL);
        }
    }
}
