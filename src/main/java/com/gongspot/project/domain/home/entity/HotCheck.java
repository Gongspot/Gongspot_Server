package com.gongspot.project.domain.home.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.place.entity.Place;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "HotCheck")
public class HotCheck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Hot_id")
    private Long id;

    @Column(name = "week")
    private Integer week;

    @Column(name = "count")
    private Long cnt;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    public void increaseCount() {
        this.cnt += 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void resetCount() {
        this.cnt = 1L;
        this.updatedAt = LocalDateTime.now();
    }
}
