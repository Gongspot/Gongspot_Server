package com.gongspot.project.domain.place.service;

public interface PlaceCommandService {
    void isLikedPlace(Long UserId, Long placeId);
    void unLikedPlace(Long UserId, Long placeId);
    void viewCongestion(Long userId, Long placeId);
}
