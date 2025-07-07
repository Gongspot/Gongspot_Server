package com.gongspot.project.domain.place.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.place.converter.PlaceConverter;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import com.gongspot.project.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceQueryServiceImpl implements PlaceQueryService{
    private final PlaceRepository placeRepository;
    private final MediaRepository mediaRepository;

    @Override
    public PlaceResponseDTO.GetPlaceDTO getPlace(Long placeId){
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.PLACE_NOT_FOUND));

        Double averageRating = mediaRepository.getAverageRatingByPlaceId(placeId);
        List<Review> congestionList = mediaRepository.findTop3ByPlaceId(placeId, PageRequest.of(0, 3));

        return PlaceConverter.toGetPlaceDTO(place, averageRating, congestionList);
    }
}
