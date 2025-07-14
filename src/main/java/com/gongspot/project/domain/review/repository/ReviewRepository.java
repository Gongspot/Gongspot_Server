package com.gongspot.project.domain.review.repository;

import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
        SELECT AVG(r.rating)
        FROM Media m
        JOIN m.review r
        WHERE m.place.id = :placeId
    """)
    Double getAverageRatingByPlaceId(@Param("placeId") Long placeId);

    @Query("""
        SELECT r FROM Media m
        JOIN m.review r
        WHERE m.place.id = :placeId
        ORDER BY r.datetime DESC
    """)
    List<Review> findTop3ByPlaceId(@Param("placeId") Long placeId, Pageable pageable);
    List<Review> findAllByPlaceAndCongestionIsNotNullOrderByDatetimeDesc(Place place, Pageable pageable);
}
