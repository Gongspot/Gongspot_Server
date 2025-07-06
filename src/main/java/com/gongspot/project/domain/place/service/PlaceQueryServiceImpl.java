package com.gongspot.project.domain.place.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.place.converter.PlaceConverter;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceQueryServiceImpl implements PlaceQueryService{
    private final PlaceRepository placeRepository;
    private final MediaRepository mediaRepository;

    @Override
    public PlaceResponseDTO.GetPlaceDTO getPlace(Long placeid){
        Place place = placeRepository.findById(placeid)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        Double averageRating = mediaRepository.getAverageRatingByPlaceId(placeid);

        return PlaceConverter.toGetPlaceDTO(place, averageRating);
    }
}
