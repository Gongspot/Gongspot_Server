package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.search.converter.RecentSearchConverter;
import com.gongspot.project.domain.search.dto.RecentSearchResponseDTO;
import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.search.repository.RecentSearchRepository;
import com.gongspot.project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchQueryServiceImpl implements RecentSearchQueryService {

    private final RecentSearchRepository recentSearchRepository;

    @Override
    @Transactional(readOnly = true)
    public RecentSearchResponseDTO.RecentSearchViewResponseDTO getRecentSearches(User user) {

        // 1. 데이터베이스에서 최신 검색어 엔티티 3개를 가져옵니다.
        List<RecentSearch> recentSearches = recentSearchRepository
                .findTop3ByUserOrderByUpdatedAtDesc(user);

        // 2. 컨버터를 사용하여 엔티티 리스트를 원하는 응답 DTO로 매핑합니다.
        return RecentSearchConverter.toViewDTO(recentSearches);
    }
}
