package com.gongspot.project.domain.home.service;

import com.gongspot.project.domain.home.dto.HomeResponseDTO;

import java.util.List;

public interface HomeQueryService {
    List<HomeResponseDTO.HotPlaceDTO> getHotPlaceList();
    List<HomeResponseDTO.CategoryPlaceDTO> getCategoryPlaceList(Integer categoryId, List<Long> excludeIdsList);
}
