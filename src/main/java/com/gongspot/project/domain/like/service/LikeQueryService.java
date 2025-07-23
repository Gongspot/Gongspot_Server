package com.gongspot.project.domain.like.service;

import com.gongspot.project.domain.like.dto.LikeResponseDTO;

public interface LikeQueryService {
    LikeResponseDTO.LikedPlaceListDTO getLikedPlaces(Long userId, String isFree);
}
