package com.gongspot.project.domain.notification.service;

import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;

public interface NotificationQueryService {
    NotificationResponseDTO.NotificationListDTO getNotificationList();
    NotificationResponseDTO.NotificationBannerListDTO getNotificationBannerList(String type);
    NotificationResponseDTO.NotificationDetailDTO getNotificationDetail(Long notificationId);
}
