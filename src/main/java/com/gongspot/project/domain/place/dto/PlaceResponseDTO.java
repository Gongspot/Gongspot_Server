package com.gongspot.project.domain.place.dto;

import com.gongspot.project.common.enums.PlaceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PlaceResponseDTO {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPlaceDTO {
        Long placeid;
        String name;
        Boolean isfree;
        Double rating;
        List<PlaceEnum> placetypes;
        String information;
    }
}
