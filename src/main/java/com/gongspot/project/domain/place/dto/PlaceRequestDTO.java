package com.gongspot.project.domain.place.dto;

import com.gongspot.project.common.enums.*;
import lombok.Getter;

import java.util.List;

public class PlaceRequestDTO {
    @Getter
    public static class PlacePatchDTO {
        String locationInfo;
        String openingHours;
        String phoneNumber;
        List<PurposeEnum> purposeList;
        PlaceEnum type;
        List<MoodEnum> moodList;
        List<FacilitiesEnum> facilityList;
        List<LocationEnum> locationList;
    }
}
