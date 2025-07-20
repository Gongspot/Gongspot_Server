package com.gongspot.project.domain.user.entity;


import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.common.enums.LocationEnum;
import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 12, nullable = false, unique = true)
    private String nickname;

    @Column(length = 1000)
    private String profileImg;

    @ElementCollection
    @CollectionTable(name = "user_prefer_place", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "value", columnDefinition = "ENUM('도서관','카페','민간학습공간','교내학습공간','공공학습공간')")
    @Enumerated(EnumType.STRING)
    private List<PlaceEnum> preferPlace;

    @ElementCollection
    @CollectionTable(name = "user_purpose", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "value", columnDefinition = "ENUM('개인공부','그룹공부','집중공부','휴식','노트북작업')")
    @Enumerated(EnumType.STRING)
    private List<PurposeEnum> purpose;

    @ElementCollection
    @CollectionTable(name = "user_location", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "value", columnDefinition = "ENUM('강남권','강북권','도심권','성동_광진권','서남권','서북권','동남권')")
    @Enumerated(EnumType.STRING)
    private List<LocationEnum> location;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfile(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
