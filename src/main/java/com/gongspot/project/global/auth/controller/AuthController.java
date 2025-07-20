package com.gongspot.project.global.auth.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.global.auth.TokenBlacklistService;
import com.gongspot.project.global.auth.JwtTokenProvider;

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
    public ResponseEntity<Void> redirectToKakao() {
        URI kakaoUri = URI.create("http://localhost:8080/oauth2/authorization/kakao");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(kakaoUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃 (토큰 블랙리스트 등록)",
            description = "헤더에 Authorization: Bearer {access_token} 형식의 JWT를 포함하여 요청하면, 해당 토큰을 블랙리스트에 등록하여 무효화합니다.",
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
        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            tokenBlacklistService.blacklistToken(token);
        }

        return ApiResponse.onSuccess(null);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
