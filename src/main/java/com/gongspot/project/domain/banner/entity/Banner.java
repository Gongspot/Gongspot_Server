package com.gongspot.project.domain.banner.entity;

import com.gongspot.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Banners")
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    @Column(name = "img_url", length = 100)
    private String imgUrl;

    @Column(name = "title", length = 20)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;
}
