package com.gongspot.project.global.auth;

import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;

    private final long validityInMilliseconds = 1000 * 60 * 60; // 1시간

    public JwtTokenProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Long userId, String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String userIdStr = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        Long userId = Long.valueOf(userIdStr);
        User user = userService.findById(userId);

        return new UsernamePasswordAuthenticationToken(user, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
