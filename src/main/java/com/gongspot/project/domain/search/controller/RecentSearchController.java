package com.gongspot.project.domain.search.controller;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.search.dto.RecentSearchRequestDTO;
import com.gongspot.project.domain.search.dto.RecentSearchResponseDTO;
import com.gongspot.project.domain.search.service.RecentSearchQueryService;
import com.gongspot.project.domain.search.service.RecentSearchCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recent-searches")
public class RecentSearchController {
    private final RecentSearchQueryService recentSearchQueryService;
    private final RecentSearchCommandService recentSearchCommandService;
    // 최근 검색어 조회
    @GetMapping
    public ApiResponse<RecentSearchResponseDTO.RecentSearchViewResponseDTO> getRecentSearches() {
        Long userId = getCurrentUserId();
        RecentSearchResponseDTO.RecentSearchViewResponseDTO response =
                recentSearchQueryService.getRecentSearches(userId);
        return ApiResponse.onSuccess(response);
    }
    // 최근 검색어 삭제 (복수 키워드 한 번에 삭제 가능)
    @DeleteMapping
    public ApiResponse<Void> deleteRecentSearches(
            @RequestBody RecentSearchRequestDTO.RecentSearchDeleteRequestDTO request) {
        Long userId = getCurrentUserId();
        recentSearchCommandService.deleteRecentSearches(userId, request.getKeywords());
        return ApiResponse.onSuccess(null);
    }
    // 검색어 저장 (검색 실행 시 호출)
    @PostMapping
    public ApiResponse<Void> saveRecentSearch(
            @RequestBody RecentSearchRequestDTO.RecentSearchSaveRequestDTO request) {
        Long userId = getCurrentUserId();
        recentSearchCommandService.saveOrUpdateSearch(userId, request.getKeywords());
        return ApiResponse.onSuccess(null);
    }
    // JWT 토큰에서 사용자 ID 추출
    private Long getCurrentUserId() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.valueOf(userIdStr);
    }
}