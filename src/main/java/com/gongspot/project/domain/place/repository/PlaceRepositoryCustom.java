package com.gongspot.project.domain.place.repository;

import com.gongspot.project.common.enums.*;
import com.gongspot.project.domain.place.dto.PlaceRequestDTO;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<PlaceResponseDTO.SearchPlaceDTO> findFilteredPlaces(Long userId, String keyword, List<PurposeEnum> purpose, PlaceEnum type, List<MoodEnum> mood, List<FacilitiesEnum> facilities, List<LocationEnum> location, Long page);
}
