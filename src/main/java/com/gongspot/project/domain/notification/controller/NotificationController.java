package com.gongspot.project.domain.notification.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.notification.dto.NotificationRequestDTO;
import com.gongspot.project.domain.notification.service.NotificationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;
import com.gongspot.project.domain.notification.service.NotificationCommandService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Tag(name = "공지사항", description = "공지사항 관련 API")
@Validated
public class NotificationController {

    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;

    @Operation(summary = "공지사항 목록 조회")
    @GetMapping
    public ApiResponse<NotificationResponseDTO.NotificationListDTO> getNotificationList() {
        NotificationResponseDTO.NotificationListDTO result = notificationQueryService.getNotificationList();
        return ApiResponse.onSuccess(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항/배너 목록 조회(관리자)")
    @GetMapping("/admin")
    public ApiResponse<NotificationResponseDTO.NotificationBannerListDTO> getNotificationBannerList(@RequestParam(value = "type", required = false, defaultValue = "ALL") String type) {
        NotificationResponseDTO.NotificationBannerListDTO result = notificationQueryService.getNotificationBannerList(type);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "공지사항 상세 조회")
    @GetMapping("/{notificationId}")
    public ApiResponse<NotificationResponseDTO.NotificationDetailDTO> getNotificationDetail(
            @PathVariable("notificationId") Long notificationId) {
        NotificationResponseDTO.NotificationDetailDTO result = notificationQueryService.getNotificationDetail(notificationId);
        return ApiResponse.onSuccess(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 작성 (관리자)")
    @PostMapping(value = "/{category}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> createNotification(
            @Parameter(description = "카테고리: B는 배너, N은 공지사항", example = "N")
            @PathVariable("category") String category,

            @Parameter(
                    description = "공지사항 JSON 본문",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NotificationRequestDTO.class)
                    )
            )
            @Valid @RequestPart("request") NotificationRequestDTO requestDTO,

            @Parameter(description = "첨부파일 리스트 (선택)")
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,

            @Parameter(description = "썸네일 이미지 파일 (선택)")
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile
    ) {
        if (attachments == null) attachments = List.of();
        notificationCommandService.createNotification(category, requestDTO, attachments, thumbnailFile);
        return ApiResponse.onSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 수정 (관리자)")
    @PatchMapping(value = "/{notificationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateNotification(
            @Parameter(description = "공지사항 ID")
            @PathVariable("notificationId") Long notificationId,

            @Parameter(
                    description = "수정할 본문 내용(JSON)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NotificationRequestDTO.class)
                    )
            )
            @Valid @RequestPart("request") NotificationRequestDTO requestDTO,

            @Parameter(description = "삭제할 첨부파일 ID 리스트 (선택)")
            @RequestPart(value = "attachmentIdsToDelete", required = false) List<Long> mediaIdsToDelete,

            @Parameter(description = "새로 추가할 첨부파일들 (선택)")
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        if (attachments == null) attachments = List.of();
        notificationCommandService.updateNotification(notificationId, requestDTO, mediaIdsToDelete, attachments);
        return ApiResponse.onSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 삭제 (관리자)")
    @DeleteMapping("/{notificationId}")
    public ApiResponse<String> deleteNotification(
            @Parameter(description = "삭제할 공지사항 ID")
            @PathVariable("notificationId") Long notificationId
    ) {
        notificationCommandService.deleteNotification(notificationId);
        return ApiResponse.onSuccess();
    }
}