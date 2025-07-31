package com.gongspot.project.domain.home.repository;

import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.domain.home.dto.HomeResponseDTO;
import com.gongspot.project.domain.place.entity.Place;

import java.util.List;

public interface HomeRepositoryCustom {
    List<HomeResponseDTO.HotPlaceDTO> findTop10PlacesByWeeklyVisits(Long userId);
    List<HomeResponseDTO.CategoryPlaceDTO> findRandomPlacesExcluding(Long userId, PlaceEnum placeType, List<Long> excludeIds);
}
