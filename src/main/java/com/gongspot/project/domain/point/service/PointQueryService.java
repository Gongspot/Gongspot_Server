package com.gongspot.project.domain.point.service;

import com.gongspot.project.domain.point.dto.PointResponseDTO;

public interface PointQueryService {
    PointResponseDTO getTotalPoints(Long userId);
}