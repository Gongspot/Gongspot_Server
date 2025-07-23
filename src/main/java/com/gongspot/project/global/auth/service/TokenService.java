package com.gongspot.project.global.auth.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.user.service.UserService;
import com.gongspot.project.global.auth.entity.RefreshToken;
import com.gongspot.project.global.auth.repository.RefreshTokenRepository;
import com.gongspot.project.global.auth.JwtTokenProvider;
import com.gongspot.project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public TokenPair generateAndSaveTokens(User user) {
        // 1. Access & Refresh Token 생성
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 2. Refresh Token 만료일 계산
        LocalDateTime expiry = LocalDateTime.now().plusDays(14); // refresh 토큰 유효기간

        // 3. DB에 저장 (있으면 갱신, 없으면 생성)
        Optional<RefreshToken> existing = refreshTokenRepository.findByUserId(user.getId());

        if (existing.isPresent()) {
            existing.get().update(refreshToken, expiry);
        } else {
            RefreshToken newToken = RefreshToken.builder()
                    .userId(user.getId())
                    .token(refreshToken)
                    .expiredAt(expiry)
                    .build();
            refreshTokenRepository.save(newToken);
        }

        // 4. 토큰 반환
        return new TokenPair(accessToken, refreshToken);
    }

    public record TokenPair(String accessToken, String refreshToken) {}

    public String reissueAccessToken(Long userId, String refreshToken) {
        RefreshToken stored = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TOKEN_INVALID));

        if (!stored.getToken().equals(refreshToken)) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        if (stored.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        User user = userService.findById(userId);
        return jwtTokenProvider.createToken(user.getId(), user.getEmail()); // 새 Access Token
    }

    @Transactional
    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

}
