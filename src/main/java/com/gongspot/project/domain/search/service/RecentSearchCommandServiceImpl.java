package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.search.repository.RecentSearchRepository;
import com.gongspot.project.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchCommandServiceImpl implements RecentSearchCommandService {

    private static final int LIMIT = 3;

    private final RecentSearchRepository recentSearchRepository;

    /* ────────────── 저장 / 업데이트 ────────────── */
    @Override
    @Transactional
    public void saveOrUpdateSearch(User user, String keyword) {

        String k = keyword.trim().replaceAll("\\s+", " ");     // 공백 정리

        // ① 이미 있으면 updated_at만 갱신, 없으면 새로 insert
        recentSearchRepository.findByUserAndKeyword(user, k)
                .ifPresentOrElse(
                        RecentSearch::updateSearchTime,
                        () -> recentSearchRepository.save(
                                RecentSearch.builder()
                                        .user(user)
                                        .keyword(k)
                                        .build())
                );

        // ② 3개 초과분 정리
        List<RecentSearch> all = recentSearchRepository.findByUserOrderByUpdatedAtDesc(user);
        if (all.size() > LIMIT) {
            List<String> excess = all.stream()
                    .skip(LIMIT)          // 4번째 이후
                    .map(RecentSearch::getKeyword)
                    .toList();
            recentSearchRepository.deleteByUserAndKeywordIn(user, excess);
        }
    }

    /* ────────────── 다건 삭제 ────────────── */
    @Override
    @Transactional
    public void deleteRecentSearch(User user, Long searchId) {

        recentSearchRepository.findByIdAndUser(searchId, user)
                .ifPresentOrElse(recentSearchRepository::delete,
                        () -> { throw new IllegalArgumentException("search_id 가 잘못되었습니다."); });
    }
}
