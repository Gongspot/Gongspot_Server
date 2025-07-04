package com.gongspot.project.domain.notification.entity;

import com.gongspot.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
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
}
