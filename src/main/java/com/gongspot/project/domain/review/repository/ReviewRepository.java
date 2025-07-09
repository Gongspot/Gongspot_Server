package com.gongspot.project.domain.review.repository;

import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.review.entity.Review;
import com.gongspot.project.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
        SELECT AVG(r.rating)
        FROM Review r
        WHERE r.place.id = :placeId
    """)
    Double getAverageRatingByPlaceId(@Param("placeId") Long placeId);

    @Query("""
        SELECT r
        FROM Review r
        WHERE r.place.id = :placeId
        ORDER BY r.datetime DESC
    """)
    List<Review> findTop3ByPlaceId(@Param("placeId") Long placeId, Pageable pageable);

    Page<Review> findAllByPlaceOrderByCreatedAtDesc(Place place, Pageable pageable);
    Optional<Review> findByUserAndPlace(User user, Place place);
    List<Review> findAllByPlace(Place place);
}
