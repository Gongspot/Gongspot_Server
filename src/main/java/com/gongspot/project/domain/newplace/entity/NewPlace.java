package com.gongspot.project.domain.newplace.entity;

import com.gongspot.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "NewPlaces")
public class NewPlace extends BaseEntity {
    // 장소 요청 시 사용하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "np_id")
    private Long id;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "link", length = 1000)
    private String link;

    @Lob
    @Column(name = "reason")
    private String reason;

     public NewPlace(String name, String link, String reason) {
         this.name = name;
         this.link = link;
         this.reason = reason;
     }

}
