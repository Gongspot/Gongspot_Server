package com.gongspot.project.domain.home.repository;

import com.gongspot.project.domain.home.dto.HomeResponseDTO;

import java.util.List;

public interface HomeRepositoryCustom {
    List<HomeResponseDTO.HotPlaceDTO> findTop10PlacesByWeeklyVisits();
}
