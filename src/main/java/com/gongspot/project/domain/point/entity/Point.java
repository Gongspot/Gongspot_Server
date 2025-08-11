package com.gongspot.project.domain.point.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Points")
public class Point extends BaseEntity {
    // 포인트 내역 관리하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "point_update", nullable = false)
    private Integer updatedPoint;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "content", length = 20)
    private String content;
}