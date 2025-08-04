package com.gongspot.project.domain.notification.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.media.entity.Media;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name = "Notification")
public class Notification extends BaseEntity {
    // 공지사항 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "title", length = 20)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<Media> mediaList = new ArrayList<>();
}
