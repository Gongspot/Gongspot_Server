package com.gongspot.project.domain.home.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.place.entity.Place;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
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

    @OneToOne
    @JoinColumn(name = "place_id")
    private Place place;
}
