package com.gongspot.project.domain.user.dto;

import com.gongspot.project.common.enums.LocationEnum;
import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
        MultipartFile profileImg;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreferRequestDTO {

        // @Size(max = 3, message = "선호 장소는 최대 3개까지 선택할 수 있습니다.")
        @NotNull(message = "선호 장소 리스트는 비어있을 수 없습니다.")
        private List<PlaceEnum> preferPlace;

        // @Size(max = 3, message = "목적은 최대 3개까지 선택할 수 있습니다.")
        @NotNull(message = "목적 리스트는 비어있을 수 없습니다.")
        private List<PurposeEnum> purpose;

        // @Size(max = 3, message = "위치는 최대 3개까지 선택할 수 있습니다.")
        @NotNull(message = "위치 리스트는 비어있을 수 없습니다.")
        private List<LocationEnum> location;
    }
}
