package com.gongspot.project.domain.place.converter;

import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;

public class PlaceConverter {

    public static PlaceResponseDTO.GetPlaceDTO toGetPlaceDTO(Place place, Double rating) {
        return PlaceResponseDTO.GetPlaceDTO.builder()
                .placeid(place.getId())
                .name(place.getName())
                .isfree(place.getIsFree())
                .rating(rating)
                .placetypes(place.getType())
                .information(place.getInformation())
                .build();
    }
}
