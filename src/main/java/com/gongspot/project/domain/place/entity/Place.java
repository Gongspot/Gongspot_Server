package com.gongspot.project.domain.place.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.common.enums.FacilitiesEnum;
import com.gongspot.project.common.enums.LocationEnum;
import com.gongspot.project.common.enums.MoodEnum;
import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.common.enums.PurposeEnum;

import com.gongspot.project.domain.home.entity.HotCheck;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "Places",
        indexes = {
                @Index(name = "idx_place_name", columnList = "name"),
                @Index(name = "idx_place_is_free", columnList = "is_free")
        }
)
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @ElementCollection
    @CollectionTable(name = "place_purpose", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "value", columnDefinition = "ENUM('개인공부','그룹공부','집중공부','휴식','노트북작업')")
    @Enumerated(EnumType.STRING)
    private List<PurposeEnum> purpose;

    @Column(name = "type", columnDefinition = "ENUM('도서관','카페','민간학습공간','교내학습공간','공공학습공간')")
    @Enumerated(EnumType.STRING)
    private PlaceEnum type;

    @ElementCollection
    @CollectionTable(name = "place_mood", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "value", columnDefinition = "ENUM('넓은','아늑한','깔끔한','조용한','음악이_나오는','이야기를_나눌_수_있는')")
    @Enumerated(EnumType.STRING)
    private List<MoodEnum> mood;

    @ElementCollection
    @CollectionTable(name = "place_facilities", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "value", columnDefinition = "ENUM('WiFi','콘센트','넓은_좌석','음료')")
    @Enumerated(EnumType.STRING)
    private List<FacilitiesEnum> facilities;

    @ElementCollection
    @CollectionTable(name = "place_location", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "value", columnDefinition = "ENUM('강남권','강북권','도심권','성동_광진권','서남권','서북권','동남권')")
    @Enumerated(EnumType.STRING)
    private List<LocationEnum> location;

    @Column(name = "is_free")
    private Boolean isFree;

    @Lob
    @Column(name = "photoUrl", columnDefinition = "TEXT")
    private String photoUrl;

    @Column(name = "location")
    private String locationInfo;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "phone_num")
    private String phoneNumber;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<HotCheck> hotCheckList = new ArrayList<>();
}