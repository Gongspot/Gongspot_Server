package com.gongspot.project.domain.search.service;

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

        List<String> keywords = recentSearchRepository
                .findTop3ByUserOrderByUpdatedAtDesc(user)   // DB에서 바로 최신 3건
                .stream()
                .map(RecentSearch::getKeyword)
                .toList();

        return RecentSearchResponseDTO.RecentSearchViewResponseDTO.builder()
                .keywords(keywords)
                .build();
    }
}
