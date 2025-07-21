package com.gongspot.project.global.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, length = 512)
    private String token;

    private LocalDateTime expiredAt;

    public void update(String newToken, LocalDateTime newExpiry) {
        this.token = newToken;
        this.expiredAt = newExpiry;
    }
}
