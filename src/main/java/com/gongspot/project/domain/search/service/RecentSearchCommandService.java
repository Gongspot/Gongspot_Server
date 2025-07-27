/* RecentSearchCommandService.java */
package com.gongspot.project.domain.search.service;

import com.gongspot.project.domain.user.entity.User;

import java.util.List;

public interface RecentSearchCommandService {
    void saveOrUpdateSearch(User user, String keyword);
    void deleteRecentSearches(User user, List<String> keywords);
}
