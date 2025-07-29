package com.gongspot.project.domain.search.repository;

import com.gongspot.project.domain.search.entity.RecentSearch;
import com.gongspot.project.domain.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {

    // 최신순(업데이트 시간)
    List<RecentSearch> findByUserOrderByUpdatedAtDesc(User user);

    // userId + keyword 중복 체크
    Optional<RecentSearch> findByUserAndKeyword(User user, String keyword);

    // 한 번에 N개 키워드 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM RecentSearch rs WHERE rs.user = :userId AND rs.keyword IN :keywords")
    void deleteByUserAndKeywordIn(@Param("userId") User user,
                                    @Param("keywords") List<String> keywords);

    // userId 전체 삭제
    void deleteByUser(User user);

    long countByUser(User user);

    // retentionDays 관리용
    @Modifying
    @Transactional
    @Query("DELETE FROM RecentSearch rs WHERE rs.updatedAt < :cutoffDate")
    void deleteOldSearches(@Param("cutoffDate") LocalDateTime cutoffDate);

    // 최신 3개 조회 (native)
    @Query(value = """
            SELECT *
              FROM recent_search
             WHERE user_id = :userId
             ORDER BY updated_at DESC
             LIMIT :limit
            """, nativeQuery = true)
    List<RecentSearch> findRecentSearchesWithLimitNative(@Param("userId") Long userId,
                                                         @Param("limit") int limit);
}
