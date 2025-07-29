package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.search.repository.RecentSearchRepository;
import com.gongspot.project.domain.search.service.RecentSearchCommandService;
import com.gongspot.project.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchCommandServiceImpl implements RecentSearchCommandService {

    private static final int LIMIT = 3;

    private final RecentSearchRepository recentSearchRepository;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher publisher;

    private String redisKey(User user) {          // redis key
        return "recent:" + user.getId();
    }

    @Override
    @Transactional
    public void saveOrUpdateSearch(User user, String keyword) {
        String k = keyword.trim().replaceAll("\\s+"," ");


        recentSearchRepository.findByUserAndKeyword(user, k)
                .ifPresentOrElse(RecentSearch::updateSearchTime,         // 이미 있으면 updatedAt만 수정
                        () -> recentSearchRepository.save(
                                RecentSearch.builder()
                                        .user(user)
                                        .keyword(k)
                                        .build())
                );
        String key = redisKey(user);
        // 1) 중복 제거 (REMOVE 후 PUSH)
        redisTemplate.opsForList().remove(key, 0, k);

        // 2) 최신걸 왼쪽에 추가
        redisTemplate.opsForList().leftPush(key, k);

        // 3) 3개 초과 잘라내기
        redisTemplate.opsForList().trim(key, 0, LIMIT - 1);
    }

    @Override
    @Transactional
    public void deleteRecentSearches(User user, List<String> keywords) {

        recentSearchRepository.deleteByUserAndKeywordIn(user, keywords);

        // Redis 삭제
        String key = redisKey(user);

        keywords.forEach(kw -> redisTemplate.opsForList().remove(key, 0, kw));

    }
}
