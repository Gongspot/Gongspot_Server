package com.gongspot.project.domain.point.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointResponseDTO {

        private Long userId;
        private Long totalPoints;
}
