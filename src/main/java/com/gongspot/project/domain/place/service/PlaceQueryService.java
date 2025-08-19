package com.gongspot.project.domain.place.service;

import com.gongspot.project.common.enums.*;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;

import java.util.List;

public interface PlaceQueryService {
    PlaceResponseDTO.GetPlaceDTO getPlace(Long userId, Long placeId);
    PlaceResponseDTO.VisitedPlaceListDTO getVisitedPlaces(Long userId);
    List<PlaceResponseDTO.SearchPlaceDTO> getFilteredPlaces(Long userId, String keyword, List<PurposeEnum> purpose, PlaceEnum type, List<MoodEnum> mood, List<FacilitiesEnum> facilities, List<LocationEnum> location, Long page);
    Place getPlaceDetails(Long placeId);
}
