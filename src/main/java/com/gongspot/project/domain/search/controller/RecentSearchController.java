package com.gongspot.project.domain.search.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.search.dto.RecentSearchRequestDTO;
import com.gongspot.project.domain.search.dto.RecentSearchResponseDTO;
import com.gongspot.project.domain.search.service.RecentSearchCommandService;
import com.gongspot.project.domain.search.service.RecentSearchQueryService;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recent-search")
public class RecentSearchController {

    private final RecentSearchQueryService recentSearchQueryService;
    private final RecentSearchCommandService recentSearchCommandService;
    private final UserService userService;

    //최근 검색어 조회 (항상 최신 3개만 반환, 조회 시 3개 초과분 자동 정리)
    @GetMapping
    public ApiResponse<RecentSearchResponseDTO.RecentSearchViewResponseDTO> getRecentSearches() {
        User user = getCurrentUser();
        RecentSearchResponseDTO.RecentSearchViewResponseDTO response = recentSearchQueryService.getRecentSearches(user);
        return ApiResponse.onSuccess(response);
    }

    //최근 검색어(들) 삭제
    @DeleteMapping
    public ApiResponse<Void> deleteRecentSearches(
            @RequestBody @Valid RecentSearchRequestDTO.RecentSearchDeleteRequestDTO request) {

        User user = getCurrentUser();
        recentSearchCommandService.deleteRecentSearches(user, request.getKeywords());
        return ApiResponse.onSuccess(null);
    }

    //JWT(SecurityContext)에 저장된 subject 값(ID)를 User 객체로 조회
    private User getCurrentUser() {
        Long userId = Long.valueOf(
                SecurityContextHolder.getContext().getAuthentication().getName());
        return userService.findById(userId);
    }
}
