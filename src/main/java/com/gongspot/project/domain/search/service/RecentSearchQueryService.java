/* RecentSearchQueryService.java */
package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.search.dto.RecentSearchResponseDTO;
import com.gongspot.project.domain.user.entity.User;

public interface RecentSearchQueryService {
    RecentSearchResponseDTO.RecentSearchViewResponseDTO getRecentSearches(User user);
}
