package com.gongspot.project.domain.search.repository;

import com.gongspot.project.domain.search.entity.RecentSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {
}
