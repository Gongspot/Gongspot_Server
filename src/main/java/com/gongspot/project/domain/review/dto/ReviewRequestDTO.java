package com.gongspot.project.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gongspot.project.common.enums.CongestionEnum;
import com.gongspot.project.common.enums.FacilitiesEnum;
import com.gongspot.project.common.enums.MoodEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewRequestDTO {
    @Getter
    public static class ReviewRegisterDTO {

        @NotNull(message = "날짜는 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime datetime;

        @NotNull(message = "평점은 필수입니다.")
        @DecimalMin(value = "0.0", inclusive = true)
        @DecimalMax(value = "5.0", inclusive = true)
        Integer rate;

        @NotNull(message = "혼잡도는 필수입니다.")
        CongestionEnum congest;

        @NotEmpty(message = "목적은 하나 이상 선택해야 합니다.")
        List<PurposeEnum> purpose;

        @NotEmpty(message = "분위기는 하나 이상 선택해야 합니다.")
        List<MoodEnum> mood;

        List<FacilitiesEnum> facility;

        @NotBlank(message = "리뷰 내용을 입력해주세요.")
        String content;

        Boolean like;
    }
}
