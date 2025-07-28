package com.gongspot.project.domain.home.service;

import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.domain.home.dto.HomeResponseDTO;
import com.gongspot.project.domain.home.repository.HomeRepository;
import com.gongspot.project.domain.place.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeQueryServiceImpl implements HomeQueryService {

    private final HomeRepository homeRepository;

    @Override
    public List<HomeResponseDTO.HotPlaceDTO> getHotPlaceList(Long userId) {
        return homeRepository.findTop10PlacesByWeeklyVisits(userId);
    }

    @Override
    public List<HomeResponseDTO.CategoryPlaceDTO> getCategoryPlaceList(Integer categoryId, List<Long> excludeIdsList) {
        PlaceEnum placeType = null;
        if (categoryId != null) {
            PlaceEnum[] values = PlaceEnum.values();
            if (categoryId >= 1 && categoryId <= values.length) {
                placeType = values[categoryId - 1];
            }
        }
        return homeRepository.findRandomPlacesExcluding(placeType, excludeIdsList);
    }
}
