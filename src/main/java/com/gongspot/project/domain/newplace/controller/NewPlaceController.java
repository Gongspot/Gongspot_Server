package com.gongspot.project.domain.newplace.controller;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.service.NewPlaceCommandService;
import com.gongspot.project.domain.newplace.service.GoogleMapsService;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.service.GooglePlaceDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
@Tag(name = "공간 등록", description = "공간 등록 요청 관련 API")
public class NewPlaceController {

    private final NewPlaceCommandService newPlaceCommandService;
    private final GoogleMapsService googleMapsService;
    private final GooglePlaceDetailService googlePlaceDetailService;

    @Operation(summary = "새 공간 등록 신청")
    @PostMapping("/proposal")
    public ApiResponse<NewPlaceResponseDTO.NewProposalDTO> createNewPlaceProposal(
            @RequestBody @Valid NewPlaceRequestDTO requestDTO
    ) {
        NewPlaceResponseDTO.NewProposalDTO result = newPlaceCommandService.createNewPlaceProposal(requestDTO);
        return ApiResponse.onSuccess(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공간 등록 요청 상세 조회 (관리자)")
    @GetMapping("/proposal/{proposalId}")
    public ApiResponse<NewPlaceResponseDTO.NewProposalDTO> getNewPlaceProposal(
            @PathVariable("proposalId") Long proposalId
    ) {
        NewPlaceResponseDTO.NewProposalDTO result = newPlaceCommandService.getProposal(proposalId);
        return ApiResponse.onSuccess(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공간 등록 요청 목록 조회 (관리자)")
    @GetMapping("/proposal")
    public ApiResponse<PageResponse<NewPlaceResponseDTO.NewProposalDTO>> getNewPlaces(
            @RequestParam(name = "approve",required = false) Boolean approve,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        PageResponse<NewPlaceResponseDTO.NewProposalDTO> result = newPlaceCommandService.getProposals(approve, page, size);
        return ApiResponse.onSuccess(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "구글 맵 링크로 상세정보 조회 (관리자)", description = "구글 맵 링크를 기반으로 장소 이름을 가져오며, " +
            "그를 기반으로 장소 정보를 가져옵니다. (링크 기반으로 찾을 수 없으면 이름 기반으로 서칭)")
    @GetMapping("/resolve-link")
    public ApiResponse<PlaceResponseDTO.GooglePlaceDTO> getGooglePlaceDetail(
            @RequestParam(name = "shortUrl") String shortUrl,
            @RequestParam(name = "name",required = false) String name) {
        String keyword = null;

        try {
            if (shortUrl != null && !shortUrl.isBlank()) {
                keyword = googleMapsService.resolveAndExtractKeyword(shortUrl);
            }
        } catch (Exception e) {
            // 로그 찍고 무시
            log.warn("resolveAndExtractKeyword 실패: {}", e.getMessage());
        }

        if (keyword == null || keyword.isBlank()) {
            if (name == null || name.isBlank()) {
                throw new GeneralException(ErrorStatus.INVALID_INPUT);
            }
            keyword = name;
        }

        PlaceResponseDTO.GooglePlaceDTO response = googlePlaceDetailService.searchPlaceDetail(keyword);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "장소 요청 승인 API (관리자)")
    public ApiResponse<String> approvePlace(
            @RequestParam("proposalId") Long proposalId,
            @RequestBody PlaceResponseDTO.PlaceApprovalRequestDTO requestDTO) {
        newPlaceCommandService.approveProposal(proposalId, requestDTO);
        return ApiResponse.onSuccess("장소 등록 완료");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공간 등록 홈화면 (관리자)")
    @GetMapping("/proposalHome")
    public ApiResponse<NewPlaceResponseDTO.NewProposalHomeDTO> getNewProposalHome(
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        NewPlaceResponseDTO.NewProposalHomeDTO result = newPlaceCommandService.getNewProposalHome(page);
        return ApiResponse.onSuccess(result);
    }
}