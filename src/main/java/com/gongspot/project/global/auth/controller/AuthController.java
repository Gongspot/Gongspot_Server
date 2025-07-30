package com.gongspot.project.global.auth.controller;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.global.auth.dto.KakaoUserInfoDTO;
import com.gongspot.project.global.auth.dto.TokenResponseDTO;
import com.gongspot.project.global.auth.service.OAuthKakaoService;
import com.gongspot.project.global.auth.service.TokenBlacklistService;
import com.gongspot.project.global.auth.JwtTokenProvider;

import com.gongspot.project.global.auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "JWT 인증 관련 API")
public class AuthController {

    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final OAuthKakaoService oAuthKakaoService;

    @GetMapping("/oauth/kakao/callback")
    @Operation(
            summary = "카카오 로그인 코드 콜백",
            description = """
        프론트에서 전달받은 카카오 인가 코드(code)를 사용해 
        카카오 액세스 토큰을 발급받고 사용자 정보를 조회한 후, 
        JWT accessToken과 refreshToken을 발급하여 반환합니다.
        """,
            parameters = {
                    @Parameter(name = "code", description = "카카오에서 전달받은 인가 코드", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "성공적으로 accessToken, refreshToken 발급됨",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TokenResponseDTO.class),
                                    examples = @ExampleObject(
                                            name = "Success Response",
                                            summary = "토큰 발급 성공",
                                            value = """
                    {
                      "isSuccess": true,
                      "code": "COMMON200",
                      "message": "성공입니다.",
                      "result": {
                        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                      }
                    }
                """
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "요청 실패 - 카카오 토큰/유저 정보 파싱 실패, 필수 정보 누락 등",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Invalid UserInfo",
                                            summary = "카카오 사용자 정보 누락",
                                            value = """
                    {
                      "isSuccess": false,
                      "code": "OAUTH4002",
                      "message": "카카오 계정의 이메일 정보를 찾을 수 없습니다."
                    }
                """
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Server Error",
                                            summary = "예상치 못한 서버 에러",
                                            value = """
                    {
                      "isSuccess": false,
                      "code": "INTERNAL500",
                      "message": "예상치 못한 서버 오류가 발생했습니다."
                    }
                """
                                    )
                            )
                    )
            }

    )
    public ApiResponse<TokenResponseDTO> kakaoCallback(@RequestParam String code) {
        String kakaoAccessToken = oAuthKakaoService.getAccessToken(code);
        Map<String, Object> kakaoUserInfo = oAuthKakaoService.getUserInfo(kakaoAccessToken);

        KakaoUserInfoDTO userInfo = KakaoUserInfoDTO.from(kakaoUserInfo);

        TokenService.TokenPair tokenPair = tokenService.generateTokensFromKakaoUserInfo(
                userInfo.getEmail(), userInfo.getNickname(), userInfo.getProfileImageUrl()
        );

        return ApiResponse.onSuccess(new TokenResponseDTO(tokenPair.accessToken(), tokenPair.refreshToken()));
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

        return ApiResponse.onSuccess(new TokenResponseDTO(newAccessToken, refreshToken));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
