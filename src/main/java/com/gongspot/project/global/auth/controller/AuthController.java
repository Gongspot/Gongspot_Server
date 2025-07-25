package com.gongspot.project.global.auth.controller;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.global.auth.dto.TokenResponseDTO;
import com.gongspot.project.global.auth.service.TokenBlacklistService;
import com.gongspot.project.global.auth.JwtTokenProvider;

import com.gongspot.project.global.auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "JWT 인증 관련 API")
public class AuthController {

    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    @GetMapping("/login")
    @Operation(
            summary = "카카오 로그인 리다이렉트",
            description = "이 API는 클라이언트를 카카오 로그인 페이지로 리다이렉트합니다. Swagger에선 작동하지 않지만, 브라우저에서는 정상 동작합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "302",
                            description = "카카오 로그인 페이지로의 리다이렉트",
                            headers = {
                                    @Header(name = "Location", description = "리다이렉트될 카카오 로그인 URL", schema = @Schema(type = "string"))
                            }
                    )
            }
    )
    public ResponseEntity<Void> redirectToKakao(HttpServletRequest request) {
        String scheme = request.getScheme(); // http or https
        String serverName = request.getServerName(); // localhost or domain
        int serverPort = request.getServerPort(); // 8080, 80, 443 등

        String port = "";
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            port = ":" + serverPort;
        }

        String baseUrl = scheme + "://" + serverName + port;
        String redirectUrl = baseUrl + "/oauth2/authorization/kakao";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃 (토큰 블랙리스트 등록)",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공적으로 로그아웃되었습니다.",
            headers = {
                    @Header(name = "Authorization", description = "JWT 액세스 토큰", required = true)
            }
    )
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String accessToken = resolveToken(request);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            Long userId = Long.valueOf(jwtTokenProvider.getClaims(accessToken).getSubject());

            // Access Token 블랙리스트 등록
            tokenBlacklistService.blacklistToken(accessToken);

            // Refresh Token 삭제
            tokenService.deleteRefreshTokenByUserId(userId);
        }

        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/reissue")
    @Operation(
            summary = "Access Token 재발급",
            description = "Refresh Token을 이용해 새로운 Access Token을 발급합니다.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    public ApiResponse<TokenResponseDTO> reissueToken(HttpServletRequest request,
                                                      @RequestHeader("Refresh-Token") String refreshToken) {
        String accessToken = resolveToken(request);

        // 기존 Access Token이 유효하면 블랙리스트 등록
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            tokenBlacklistService.blacklistToken(accessToken);
        }

        // Refresh Token 검증
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ApiResponse.onFailure(ErrorStatus.REFRESH_TOKEN_INVALID);
        }

        // Access Token 재발급
        Long userId = Long.valueOf(jwtTokenProvider.getClaims(refreshToken).getSubject());
        String newAccessToken = tokenService.reissueAccessToken(userId, refreshToken);

        return ApiResponse.onSuccess(new TokenResponseDTO(newAccessToken));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
