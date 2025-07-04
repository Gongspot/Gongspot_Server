package com.gongspot.project.domain.alarm.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.common.enums.AlarmTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "AlarmType")
public class AlarmType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    @Column(name = "type", columnDefinition = "ENUM('공지사항','이벤트_혜택','맞춤형_공간','키워드_알림')")
    @Enumerated(EnumType.STRING)
    private AlarmTypeEnum type;
}
