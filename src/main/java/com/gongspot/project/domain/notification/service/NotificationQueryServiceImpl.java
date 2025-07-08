package com.gongspot.project.domain.notification.service;

import com.gongspot.project.domain.notification.converter.NotificationConverter;
import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;
import com.gongspot.project.domain.notification.entity.Notification;
import com.gongspot.project.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
