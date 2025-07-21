package com.gongspot.project.domain.point.service;

import com.gongspot.project.domain.point.dto.PointResponseDTO;
import org.springframework.data.domain.Pageable;
import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.domain.point.dto.PointHistoryDTO;

public interface PointQueryService {
    PointResponseDTO getTotalPoints(Long userId);
    PageResponse<PointHistoryDTO> getPointHistory(Long userId, Pageable pageable);
}