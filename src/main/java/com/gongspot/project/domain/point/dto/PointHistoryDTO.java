package com.gongspot.project.domain.point.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PointHistoryDTO {
    private Long pointId;
    private String content;
    private Integer updatedPoint;
    private LocalDate date;
}