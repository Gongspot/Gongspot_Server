package com.gongspot.project.domain.alarm.entity;

import com.gongspot.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Keyword")
public class Keyword extends BaseEntity {
    // 아직 기획 미정, 간단한 엔티티만 구현함.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @Column(name = "keyword", length = 10)
    private String keyword;
}