package com.gongspot.project.domain.banner.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.media.entity.Media;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Banners")
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "title", length = 20)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL)
    private List<Media> mediaList = new ArrayList<>();
}
