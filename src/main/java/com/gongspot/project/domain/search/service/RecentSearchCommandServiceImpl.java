package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.search.repository.RecentSearchRepository;
import com.gongspot.project.domain.search.service.RecentSearchCommandService;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.gongspot.project.domain.user.entity.QUser.user;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecentSearchCommandServiceImpl implements RecentSearchCommandService {

    private static final int LIMIT = 3;                         // ✅ 동일하게 3
    private final RecentSearchRepository recentSearchRepository;

    @Override
    public void saveOrUpdateSearch(User user, String keyword) {

        if (user == null || !StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("user/keyword 필수");
        }
        String normalized = keyword.trim().replaceAll("\\s+", " ");

        // 1) 중복이면 updatedAt만 갱신
        Optional<RecentSearch> dup = recentSearchRepository.findByUserIdAndKeyword(user, normalized);
        if (dup.isPresent()) {
            dup.get().updateSearchTime();
            return;
        }

        // 2) 새로 저장
        recentSearchRepository.save(
                RecentSearch.builder().user(user).keyword(normalized).build());

        // 3) 개수 초과 시 오래된 순으로 삭제
        List<RecentSearch> all = recentSearchRepository.findByUserIdOrderByUpdatedAtDesc(user);
        if (all.size() > LIMIT) {
            all.stream().skip(LIMIT)                       // 4번째부터
                    .map(RecentSearch::getId)
                    .forEach(recentSearchRepository::deleteById);
        }
    }

    // 키워드 삭제
    @Override
    public void deleteRecentSearches(User user, List<String> keywords) {
        recentSearchRepository.deleteByUserIdAndKeywordIn(user, keywords);
    }
}
