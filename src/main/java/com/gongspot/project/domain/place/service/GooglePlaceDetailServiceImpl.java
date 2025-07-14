package com.gongspot.project.domain.place.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.google.maps.PlaceDetailsRequest;
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
            PlaceDetails details = PlacesApi.placeDetails(context, firstPlaceId)
                    .fields(
                            PlaceDetailsRequest.FieldMask.FORMATTED_ADDRESS,
                            PlaceDetailsRequest.FieldMask.PLACE_ID,
                            PlaceDetailsRequest.FieldMask.NAME,
                            PlaceDetailsRequest.FieldMask.PHOTOS,
                            PlaceDetailsRequest.FieldMask.GEOMETRY,
                            PlaceDetailsRequest.FieldMask.OPENING_HOURS,
                            PlaceDetailsRequest.FieldMask.SECONDARY_OPENING_HOURS,
                            PlaceDetailsRequest.FieldMask.INTERNATIONAL_PHONE_NUMBER
                    )
                    .language("ko")
                    .await();


            if (details.photos == null || details.photos.length == 0) {
                log.info("No photos for placeId: {}", firstPlaceId);
                return null;
            }

            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference="
                    + details.photos[0].photoReference
                    + "&key="
                    + apiKey;

            String geometryStr = "";
            if (details.geometry != null && details.geometry.location != null) {
                geometryStr = "lat:" + details.geometry.location.lat + ", lng:" + details.geometry.location.lng;
            }

            String openingHoursStr = "";
            if (details.openingHours != null && details.openingHours.weekdayText != null) {
                openingHoursStr = String.join(", ", details.openingHours.weekdayText);
            }

            String secondaryOpeningHoursStr = "";
            if (details.secondaryOpeningHours != null) {
                secondaryOpeningHoursStr = details.secondaryOpeningHours.toString();
            }

            return new PlaceResponseDTO.GetPlaceResponseDTO(
                    details.placeId,
                    details.name,
                    details.formattedAddress,
                    details.internationalPhoneNumber,
                    geometryStr,
                    openingHoursStr,
                    secondaryOpeningHoursStr,
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

