package com.gongspot.project.domain.place.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlaceDetailServiceImpl implements GooglePlaceDetailService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    public PlaceResponseDTO.GetPlaceResponseDTO searchPlaceDetail(String keyword) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        try {
            // Text Search API로 첫 번째 장소 검색
            PlacesSearchResponse searchResponse = PlacesApi.textSearchQuery(context, keyword).language("ko").await();

            if (searchResponse.results == null || searchResponse.results.length == 0) {
                log.info("No results found for keyword: {}", keyword);
                return null;
            }

            // 첫 번째 결과의 placeId
            String firstPlaceId = searchResponse.results[0].placeId;

            // Details API 호출
            PlaceDetails details = PlacesApi.placeDetails(context, firstPlaceId).language("ko").await();

            if (details.photos == null || details.photos.length == 0) {
                log.info("No photos for placeId: {}", firstPlaceId);
                return null;
            }

            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference="
                    + details.photos[0].photoReference
                    + "&key="
                    + apiKey;

            return new PlaceResponseDTO.GetPlaceResponseDTO(
                    details.placeId,
                    details.name,
                    details.url != null ? details.url.toString() : "",
                    details.rating,
                    photoUrl
            );

        } catch (Exception e) {
            log.error("Google API error: {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        } finally {
            context.shutdown();
        }
    }
}

