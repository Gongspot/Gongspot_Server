package com.gongspot.project.domain.point.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Points",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_points_user_place_date_content",
                columnNames = {"user_id", "place_id", "date", "content"}
        ),
        indexes = @Index(name = "idx_points_user_place_date_content",
                columnList = "user_id, place_id, date, content")
)
@Builder
public class Point extends BaseEntity {
    // 포인트 내역 관리하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(name = "point_update", nullable = false)
    private Integer updatedPoint;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "content", length = 20)
    private String content;
}