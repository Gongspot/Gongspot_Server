package com.gongspot.project.domain.search.converter;

import com.gongspot.project.domain.search.dto.RecentSearchResponseDTO;
import com.gongspot.project.domain.search.entity.RecentSearch;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

//Entity ↔ DTO 변환 전용 (정적 util)
@UtilityClass
public class RecentSearchConverter {

    public RecentSearchResponseDTO.RecentSearchViewResponseDTO toViewDTO(List<RecentSearch> entities) {
        List<String> keywords = entities.stream()
                .map(RecentSearch::getKeyword)
                .toList();           // JDK 16+ (아니면 Collectors.toList())

        return RecentSearchResponseDTO.RecentSearchViewResponseDTO.builder()
                .keywords(keywords)
                .build();
    }
}

