package com.gongspot.project.domain.place.service;

import com.gongspot.project.domain.place.dto.PlaceRequestDTO;

public interface PlaceCommandService {
    void isLikedPlace(Long UserId, Long placeId);
    void unLikedPlace(Long UserId, Long placeId);
    void viewCongestion(Long userId, Long placeId);
    void updatePlace(Long placeId, PlaceRequestDTO.PlacePatchDTO patchDTO);
}
