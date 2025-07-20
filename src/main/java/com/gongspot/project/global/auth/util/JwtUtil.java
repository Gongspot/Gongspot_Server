package com.gongspot.project.global.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final String secretKey;

    public long getRemainingTime(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().getTime() - System.currentTimeMillis();
        } catch (Exception e) {
            // log 및 fallback 처리..
            log.error("만료 시간 계산 중 에러: {}", e.getMessage());
            return 0;
        }
    }
}
