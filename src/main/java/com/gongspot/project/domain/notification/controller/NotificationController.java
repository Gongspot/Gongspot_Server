package com.gongspot.project.domain.notification.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.notification.dto.NotificationRequestDTO;
import com.gongspot.project.domain.notification.service.NotificationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
            @PathVariable("category") String category,
            @Valid @RequestPart("request") NotificationRequestDTO requestDTO,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        if (attachments == null) {
            attachments = List.of();
        }

        notificationCommandService.createNotification(category, requestDTO, attachments);
        return ApiResponse.onSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 수정 (관리자)")
    @PatchMapping(value = "/{notificationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateNotification(
            @PathVariable("notificationId") Long notificationId,
            @Valid @RequestPart("request") NotificationRequestDTO requestDTO,
            @RequestPart(value = "attachmentIdsToDelete", required = false) List<Long> mediaIdsToDelete,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        if (attachments == null) {
            attachments = List.of();
        }

        notificationCommandService.updateNotification(notificationId, requestDTO,mediaIdsToDelete, attachments);
        return ApiResponse.onSuccess();
    }
}