package com.gongspot.project.domain.newplace.controller;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.repository.NewPlaceRepository;
import com.gongspot.project.domain.newplace.service.NewPlaceCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Tag(name = "공간 등록", description = "공간 등록 요청 관련 API")
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

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공간 등록 요청 상세 조회")
    @GetMapping("/proposal/{proposalId}")
    public ApiResponse<NewPlaceResponseDTO> getNewPlaceProposal(
            @PathVariable Long proposalId
    ) {
        NewPlaceResponseDTO result = newPlaceCommandService.getProposal(proposalId);
        return ApiResponse.onSuccess(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
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