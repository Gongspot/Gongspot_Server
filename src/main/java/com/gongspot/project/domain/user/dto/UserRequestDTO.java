package com.gongspot.project.domain.user.dto;

import com.gongspot.project.common.enums.LocationEnum;
import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NicknameRequestDTO {
        String nickname;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileRequestDTO {
        String nickname;
        String profileImg;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreferRequestDTO {
        @NotNull(message = "선호 장소 리스트는 비어있을 수 없습니다.")
        private List<PlaceEnum> preferPlace;

        @NotNull(message = "목적 리스트는 비어있을 수 없습니다.")
        private List<PurposeEnum> purpose;

        @NotNull(message = "위치 리스트는 비어있을 수 없습니다.")
        private List<LocationEnum> location;
    }
}
