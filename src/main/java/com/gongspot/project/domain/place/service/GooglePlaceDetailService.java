package com.gongspot.project.domain.place.service;

import com.gongspot.project.domain.place.dto.PlaceResponseDTO;

public interface GooglePlaceDetailService {
    PlaceResponseDTO.GooglePlaceDTO searchPlaceDetail(String keyword);
}
