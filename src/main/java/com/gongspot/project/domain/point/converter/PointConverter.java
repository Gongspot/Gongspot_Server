package com.gongspot.project.domain.point.converter;

import com.gongspot.project.domain.point.dto.PointHistoryDTO;
import com.gongspot.project.domain.point.entity.Point;

public class PointConverter {
    public static PointHistoryDTO toHistoryDTO(Point point) {
        return PointHistoryDTO.builder()
                .pointId(point.getId())
                .content(point.getContent())
                .updatedPoint(point.getUpdatedPoint())
                .date(point.getDate())
                .build();
    }
}
