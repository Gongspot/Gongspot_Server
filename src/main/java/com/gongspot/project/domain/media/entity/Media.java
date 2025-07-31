package com.gongspot.project.domain.media.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.banner.entity.Banner;
import com.gongspot.project.domain.notification.entity.Notification;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = true)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id", nullable = true)
    private Banner banner;

    @Column(name = "url", length = 1000)
    private String url;

    private String originalFileName;
    private String contentType;
}
