package com.gongspot.project.domain.point.repository;

import com.gongspot.project.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("SELECT COALESCE(SUM(p.updatedPoint), 0) FROM Point p WHERE p.user.id = :userId")
    Long getTotalPointsByUserId(@Param("userId")Long userId);

    Page<Point> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    boolean existsByUserIdAndPlaceIdAndContentAndDate(
            Long userId, Long placeId, String content, LocalDate date
    );
}