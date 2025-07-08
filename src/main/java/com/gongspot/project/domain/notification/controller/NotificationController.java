package com.gongspot.project.domain.notification.controller;

import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.notification.service.NotificationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Tag(name = "Notification")
@Validated
public class NotificationController {

    private final NotificationQueryService notificationQueryService;

    @Operation(summary = "공지사항 목록 조회")
    @GetMapping("")
    public ApiResponse<NotificationResponseDTO.NotificationListDTO> getNotificationList() {
        NotificationResponseDTO.NotificationListDTO result = notificationQueryService.getNotificationList();
        return ApiResponse.onSuccess(result);
    }

}
