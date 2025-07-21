package com.gongspot.project.domain.newplace.controller;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.entity.NewPlace;
import com.gongspot.project.domain.newplace.repository.NewPlaceRepository;
import com.gongspot.project.domain.newplace.service.NewPlaceCommandService;
import com.gongspot.project.domain.point.dto.PointHistoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class NewPlaceController {

    private final NewPlaceCommandService newPlaceCommandService;
    private final NewPlaceRepository newPlaceRepository;

    @Operation(summary = "새 공간 등록 신청")
    @PostMapping("/proposal")
    public ApiResponse<NewPlaceResponseDTO> createNewPlaceProposal(
            @RequestBody @Valid NewPlaceRequestDTO requestDTO
    ) {
        NewPlaceResponseDTO result = newPlaceCommandService.createNewPlaceProposal(requestDTO);
        return ApiResponse.onSuccess(result);
    };

    @Operation(summary = "공간 등록 요청 상세 조회")
    @GetMapping("/proposal/{proposalId}")
    public ApiResponse<NewPlaceResponseDTO> getNewPlaceProposal(
            @PathVariable Long proposalId
    ) {
        NewPlaceResponseDTO result = newPlaceCommandService.getProposal(proposalId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "공간 등록 요청 목록 조회 (관리자)")
    @GetMapping("/proposal")
    public ApiResponse<PageResponse<NewPlaceResponseDTO>> getNewPlaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageResponse<NewPlaceResponseDTO> result = newPlaceCommandService.getProposals(page, size);
        return ApiResponse.onSuccess(result);
    }
}