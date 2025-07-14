package com.gongspot.project.domain.notification.service;

import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.notification.converter.NotificationConverter;
import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;
import com.gongspot.project.domain.notification.entity.Notification;
import com.gongspot.project.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gongspot.project.common.code.status.ErrorStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService{
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO.NotificationListDTO getNotificationList() {
        List<Notification> notificationList = notificationRepository.findAllByOrderByCreatedAtDesc();

        return NotificationConverter.toNotificationListResultDTO(notificationList);
    }

    @Override
    public NotificationResponseDTO.NotificationDetailDTO getNotificationDetail(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.NOTIFICATION_NOT_FOUND));


        return NotificationConverter.toNotificationDetailDTO(notification);
    }
}
