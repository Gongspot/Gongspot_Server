package com.gongspot.project.domain.media.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Medias")
public class Media extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;

    // TODO: 서비스단에서 review 혹은 place 한개 무조건 연결되도록 검증해야 함

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = true)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = true)
    private Place place;

    @Column(name = "url", length = 1000)
    private String url;
}
