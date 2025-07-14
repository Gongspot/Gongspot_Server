package com.gongspot.project.domain.home.converter;

import com.gongspot.project.domain.home.dto.HomeResponseDTO;

import java.util.List;

public class HomeConverter {
    public static HomeResponseDTO.HotPlaceListDTO homeHotPlaceListDTO(List<HomeResponseDTO.HotPlaceDTO> placeList) {
        return HomeResponseDTO.HotPlaceListDTO.builder()
                .placeList(placeList)
                .build();
    }
}
