package com.gongspot.project.domain.newplace.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.service.NewPlaceCommandService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class NewPlaceController {

    private final NewPlaceCommandService newPlaceCommandService;

    @Operation(summary = "새 공간 등록 신청")
    @PostMapping("/proposal")
    public ApiResponse<NewPlaceResponseDTO> createNewPlaceProposal(
            @RequestBody @Valid NewPlaceRequestDTO requestDTO
    ) {
        NewPlaceResponseDTO result = newPlaceCommandService.createNewPlaceProposal(requestDTO);
        return ApiResponse.onSuccess(result);
    };

    @Operation(summary = "공간 등록 요청 조회")
    @GetMapping("/proposal/{proposalId}")
    public ApiResponse<NewPlaceResponseDTO> getNewPlaceProposal(
            @PathVariable Long proposalId
    ) {
        NewPlaceResponseDTO result = newPlaceCommandService.getProposal(proposalId);
        return ApiResponse.onSuccess(result);
    }
}