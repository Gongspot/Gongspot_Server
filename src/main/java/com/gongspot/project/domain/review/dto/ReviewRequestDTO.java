package com.gongspot.project.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gongspot.project.common.enums.CongestionEnum;
import com.gongspot.project.common.enums.FacilitiesEnum;
import com.gongspot.project.common.enums.MoodEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewRequestDTO {
    @Getter
    public static class ReviewRegisterDTO {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime datetime;
        Integer rate;
        CongestionEnum congest;
        List<PurposeEnum> purpose;
        List<MoodEnum> mood;
        List<FacilitiesEnum> facility;
        String content;
    }
}
