package com.gongspot.project.domain.review.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.common.enums.CongestionEnum;
import com.gongspot.project.common.enums.FacilitiesEnum;
import com.gongspot.project.common.enums.MoodEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "congestion", columnDefinition = "ENUM('높음','보통','낮음')")
    @Enumerated(EnumType.STRING)
    private CongestionEnum congestion;

    @ElementCollection
    @CollectionTable(name = "review_purpose", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "value", columnDefinition = "ENUM('개인공부','그룹공부','집중공부','휴식','노트북작업')")
    @Enumerated(EnumType.STRING)
    private List<PurposeEnum> purpose;

    @ElementCollection
    @CollectionTable(name = "review_mood", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "value", columnDefinition = "ENUM('넓은','아늑한','깔끔한','조용한','음악이_나오는','이야기를_나눌_수_있는')")
    @Enumerated(EnumType.STRING)
    private List<MoodEnum> mood;

    @ElementCollection
    @CollectionTable(name = "review_facilities", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "value", columnDefinition = "ENUM('WiFi','콘센트','넓은_좌석','음료')")
    @Enumerated(EnumType.STRING)
    private List<FacilitiesEnum> facilities;
}
