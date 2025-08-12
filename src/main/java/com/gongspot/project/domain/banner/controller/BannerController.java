package com.gongspot.project.domain.banner.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.banner.converter.BannerConverter;
import com.gongspot.project.domain.banner.dto.BannerRequestDTO;
import com.gongspot.project.domain.banner.dto.BannerResponseDTO;
import com.gongspot.project.domain.banner.service.BannerCommandService;
import com.gongspot.project.domain.banner.service.BannerQueryService;
import com.gongspot.project.domain.notification.dto.NotificationRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banners")
@Tag(name = "이벤트 배너", description = "이벤트 배너 관련 API")
public class BannerController {

    private final BannerQueryService bannerQueryService;
    private final BannerCommandService bannerCommandService;

    @GetMapping()
    @Operation(summary = "이벤트 배너 조회", description = "이벤트 배너를 조회합니다.")
    public ApiResponse<BannerResponseDTO.GetBannerListDTO> getBannerList() {
        BannerResponseDTO.GetBannerListDTO result = bannerQueryService.getBannersList();
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{bannerId}")
    @Operation(summary = "이벤트 배너 상세 조회", description = "이벤트 배너를 상세 조회합니다.")
    public ApiResponse<BannerResponseDTO.GetBannerDetailDTO> getBannerDetailList(
            @PathVariable("bannerId") Long bannerId
    ) {
        BannerResponseDTO.GetBannerDetailDTO bannerDetail = bannerQueryService.getBannerDetail(bannerId);
        return ApiResponse.onSuccess(bannerDetail);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "배너 수정 (관리자)")
    @PatchMapping(value = "/{bannerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateBanner(
            @Parameter(description = "배너 ID")
            @PathVariable("bannerId") Long bannerId,

            @Parameter(
                    description = "수정할 본문 내용(JSON)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BannerRequestDTO.class)
                    )
            )
            @Valid @RequestPart("request") BannerRequestDTO requestDTO,

            @Parameter(description = "썸네일 이미지 파일 (선택사항)")
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,

            @Parameter(description = "삭제할 첨부파일 ID 리스트 (선택)")
            @RequestPart(value = "attachmentIdsToDelete", required = false) List<Long> mediaIdsToDelete,

            @Parameter(description = "새로 추가할 첨부파일들 (선택)")
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        if (attachments == null) attachments = List.of();
        bannerCommandService.updateBanner(bannerId, requestDTO, thumbnailFile, mediaIdsToDelete, attachments);
        return ApiResponse.onSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "배너 삭제 (관리자)")
    @DeleteMapping("/{bannerId}")
    public ApiResponse<String> deleteBanner(
            @Parameter(description = "삭제할 배너 ID")
            @PathVariable("bannerId") Long bannerId
    ) {
        bannerCommandService.deleteBanner(bannerId);
        return ApiResponse.onSuccess();
    }
}
