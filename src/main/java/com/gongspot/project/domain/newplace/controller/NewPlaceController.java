package com.gongspot.project.domain.newplace.controller;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.service.NewPlaceCommandService;
import com.gongspot.project.domain.newplace.service.GoogleMapsService;
import com.gongspot.project.domain.place.converter.PlaceConverter;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import com.gongspot.project.domain.place.service.GooglePlaceDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
@Tag(name = "공간 등록", description = "공간 등록 요청 관련 API")
public class NewPlaceController {

    private final NewPlaceCommandService newPlaceCommandService;
    private final GoogleMapsService googleMapsService;
    private final GooglePlaceDetailService googlePlaceDetailService;
    private final PlaceConverter placeConverter;
    private final PlaceRepository placeRepository;

    @Operation(summary = "새 공간 등록 신청")
    @PostMapping("/proposal")
    public ApiResponse<NewPlaceResponseDTO> createNewPlaceProposal(
            @RequestBody @Valid NewPlaceRequestDTO requestDTO
    ) {
        NewPlaceResponseDTO result = newPlaceCommandService.createNewPlaceProposal(requestDTO);
        return ApiResponse.onSuccess(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공간 등록 요청 상세 조회 (관리자)")
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

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "구글 맵 링크로 상세정보 조회 (관리자)", description = "구글 맵 링크를 기반으로 장소 이름을 가져오며, 그를 기반으로 장소 정보를 가져옵니다.")
    @GetMapping("/resolve-link")
    public ApiResponse<PlaceResponseDTO.GooglePlaceDTO> getGooglePlaceDetail(@RequestParam String shortUrl) {
        String keyword = googleMapsService.resolveAndExtractKeyword(shortUrl);
        PlaceResponseDTO.GooglePlaceDTO response = googlePlaceDetailService.searchPlaceDetail(keyword);
        return ApiResponse.onSuccess(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "장소 요청 승인 API (관리자)")
    @PostMapping("/approve")
    public ApiResponse<String> approvePlace(@RequestBody PlaceResponseDTO.PlaceApprovalRequestDTO requestDTO) {
        Place place = placeConverter.convertToPlaceEntity(requestDTO);
        placeRepository.save(place);
        return ApiResponse.onSuccess("장소 등록 완료");
    }
}