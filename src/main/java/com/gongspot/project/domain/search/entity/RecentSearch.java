package com.gongspot.project.domain.search.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RecentSearch")
@EntityListeners(AuditingEntityListener.class)
public class RecentSearch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "keyword", length = 20)
    private String keyword;

    public void updateSearchTime() {
        this.updatedAt = LocalDateTime.now();
    }
}
