package com.gongspot.project.domain.media.repository;

import com.gongspot.project.domain.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query("""
        SELECT AVG(r.rating)
        FROM Media m
        JOIN m.review r
        WHERE m.place.id = :placeid
    """)
    Double getAverageRatingByPlaceId(@Param("placeid") Long placeid);
}
