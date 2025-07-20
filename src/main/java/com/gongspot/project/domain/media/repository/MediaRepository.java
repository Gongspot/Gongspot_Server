package com.gongspot.project.domain.media.repository;

import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findAllByReviewIn(List<Review> reviews);
    Optional<Media> findByReview(Review review);
}
