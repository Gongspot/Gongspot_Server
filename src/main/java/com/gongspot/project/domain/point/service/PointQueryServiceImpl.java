package com.gongspot.project.domain.point.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.point.converter.PointConverter;
import com.gongspot.project.domain.point.dto.PageResponse;
import com.gongspot.project.domain.point.dto.PointHistoryDTO;
import com.gongspot.project.domain.point.dto.PointResponseDTO;
import com.gongspot.project.domain.point.entity.Point;
import com.gongspot.project.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointQueryServiceImpl implements PointQueryService {

    private final PointRepository pointRepository;

    @Override
    @Transactional(readOnly = true)
    public PointResponseDTO getTotalPoints(Long userId) {

        if (userId == null) {
            throw new BusinessException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Long totalPoints = pointRepository.getTotalPointsByUserId(userId);

        return PointResponseDTO.builder()
                .userId(userId)
                .totalPoints(totalPoints)
                .build();
    }

    @Override
    public PageResponse<PointHistoryDTO> getPointHistory(Long userId, Pageable pageable) {
        Page<Point> pointPage = pointRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        List<PointHistoryDTO> result = pointPage.stream()
                .map(PointConverter::toHistoryDTO)
                .toList();

        return PageResponse.of(pointPage, result);
    }
}