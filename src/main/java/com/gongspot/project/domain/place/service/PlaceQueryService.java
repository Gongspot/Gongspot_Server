package com.gongspot.project.domain.place.service;

import com.gongspot.project.domain.place.dto.PlaceResponseDTO;

public interface PlaceQueryService {
    PlaceResponseDTO.GetPlaceDTO getPlace(Long userId,Long placeId);
}
