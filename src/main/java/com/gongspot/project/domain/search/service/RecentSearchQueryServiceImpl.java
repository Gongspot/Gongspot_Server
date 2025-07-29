package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.search.dto.RecentSearchResponseDTO;
import com.gongspot.project.domain.search.repository.RecentSearchRepository;
import com.gongspot.project.domain.search.service.RecentSearchQueryService;
import com.gongspot.project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchQueryServiceImpl implements RecentSearchQueryService {

    private static final int LIMIT = 3;

    private final StringRedisTemplate redisTemplate;
    private final RecentSearchRepository recentSearchRepository;

    @Override
    public RecentSearchResponseDTO.RecentSearchViewResponseDTO getRecentSearches(User user) {

        // 1) Redis 조회
        List<String> keywords = redisTemplate.opsForList()
                .range(key(user), 0, LIMIT - 1);

        // 2) 캐시 미스 → DB fallback
        if (keywords == null || keywords.isEmpty()) {
            keywords = recentSearchRepository.findByUserOrderByUpdatedAtDesc(user) // 최신순
                    .stream()
                    .map(rs -> rs.getKeyword())
                    .limit(LIMIT)
                    .toList();

            // 3) Redis 재캐싱 (있을 때만)
            if (!keywords.isEmpty()) {
                redisTemplate.delete(key(user));               // 혹시 남은 값 초기화
                // 왼쪽이 최신이 되도록 rightPushAll 을 사용해 역순 삽입하거나
                // 또는 for-loop 로 leftPush 를 써도 됩니다
                for (int i = keywords.size() - 1; i >= 0; i--) {
                    redisTemplate.opsForList().leftPush(key(user), keywords.get(i));
                }
            }
        }

        return RecentSearchResponseDTO.RecentSearchViewResponseDTO.builder()
                .keywords(keywords)
                .build();
    }

    private String key(User user) {
        return "recent:" + user.getId();
    }

}
