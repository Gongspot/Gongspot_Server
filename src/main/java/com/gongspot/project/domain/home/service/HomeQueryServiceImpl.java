package com.gongspot.project.domain.home.service;

import com.gongspot.project.domain.home.dto.HomeResponseDTO;
import com.gongspot.project.domain.home.repository.HomeRepository;
import com.gongspot.project.domain.place.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeQueryServiceImpl implements HomeQueryService {

    private final HomeRepository homeRepository;

    @Override
    public List<HomeResponseDTO.HotPlaceDTO> getHotPlaceList() {
        List<HomeResponseDTO.HotPlaceDTO> placeList = homeRepository.findTop10PlacesByWeeklyVisits();

        return placeList;
    }

    @Override
    public List<HomeResponseDTO.CategoryPlaceDTO> getCategoryPlaceList(Integer categoryId, Integer page, Integer size) {
//        List<HomeResponseDTO.CategoryPlaceDTO> placeList = homeRepository.
        return null;
    }
}
