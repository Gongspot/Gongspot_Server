package com.gongspot.project.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class HomeResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HotPlaceDTO {
        Long placeId;
        String name;
        Long totalVisits;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HotPlaceListDTO {
        List<HotPlaceDTO> placeList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryPlaceDTO {
        Long placeId;
        String name;
        Double score;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryPlaceListDTO {
        List<CategoryPlaceDTO> placeList;
    }
}
