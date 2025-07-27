/* RecentSearchQueryServiceImpl.java */
package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.search.converter.RecentSearchConverter;
import com.gongspot.project.domain.search.dto.RecentSearchResponseDTO;
import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.search.repository.RecentSearchRepository;
import com.gongspot.project.domain.search.service.RecentSearchQueryService;
import com.gongspot.project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecentSearchQueryServiceImpl implements RecentSearchQueryService {

    private static final int LIMIT = 3;                    // ✅ 항상 3개만
    private final RecentSearchRepository recentSearchRepository;

    @Override
    public RecentSearchResponseDTO.RecentSearchViewResponseDTO getRecentSearches(User user) {

        Long userId = user.getId();

        // 1) 최신 4개(=LIMIT+1)까지 가져온 뒤 초과분 정리
        List<RecentSearch> list = recentSearchRepository
                .findRecentSearchesWithLimitNative(userId, LIMIT + 1);

        if (list.size() > LIMIT) {
            List<Long> excessIds = list.stream()
                    .skip(LIMIT)          // 4번째부터
                    .map(RecentSearch::getId)
                    .toList();
            recentSearchRepository.deleteAllByIdInBatch(excessIds);
            list = list.subList(0, LIMIT);                   // 최신 3개만 남김
        }

        // 3) DTO 변환 후 반환
        return RecentSearchConverter.toViewDTO(list);
    }
}
