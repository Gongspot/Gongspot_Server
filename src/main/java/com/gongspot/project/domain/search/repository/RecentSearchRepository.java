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

    /* ────────────── 조회 ────────────── */

    // “최신 3건”만 필요할 때 편하게 쓰도록 메서드 이름을 변경
    List<RecentSearch> findTop3ByUserOrderByUpdatedAtDesc(User user);

    List<RecentSearch> findByUserOrderByUpdatedAtDesc(User user);

    // 사용자 & 키워드 중복 체크
    Optional<RecentSearch> findByUserAndKeyword(User user, String keyword);

    /* ────────────── 삭제 ────────────── */

    // 여러 키워드 한 번에 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM RecentSearch rs WHERE rs.user = :user AND rs.keyword IN :keywords")
    void deleteByUserAndKeywordIn(@Param("user") User user,
                                  @Param("keywords") List<String> keywords);

    // 사용자 전체 삭제 (테스트 등에서 사용)
    void deleteByUser(User user);

    /* ────────────── 유지보수 / 만료 ────────────── */

    long countByUser(User user);

    @Modifying  @Transactional
    @Query("DELETE FROM RecentSearch rs WHERE rs.updatedAt < :cutoffDate")
    void deleteOldSearches(@Param("cutoffDate") LocalDateTime cutoffDate);
}
