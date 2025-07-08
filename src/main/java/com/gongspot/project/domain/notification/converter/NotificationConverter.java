package com.gongspot.project.domain.notification.converter;

import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;
import com.gongspot.project.domain.notification.entity.Notification;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationConverter {
    public static NotificationResponseDTO.NotificationItemDTO toNotificationItemDTO(Notification notification) {

        String formattedDate = notification.getDate().format(DateTimeFormatter.ofPattern("yy.MM.dd"));

        return NotificationResponseDTO.NotificationItemDTO.builder()
                .notificationId(notification.getId())
                .date(formattedDate)
                .title(notification.getTitle())
                .build();
    }

    public static NotificationResponseDTO.NotificationListDTO toNotificationListResultDTO(List<Notification> notificationList) {
        List<NotificationResponseDTO.NotificationItemDTO> notificationItemDTOList = notificationList.stream()
                .map(NotificationConverter::toNotificationItemDTO)
                .collect(Collectors.toList());

        return NotificationResponseDTO.NotificationListDTO.builder()
                .notificationList(notificationItemDTOList)
                .build();
    }

    public static NotificationResponseDTO.NotificationDetailDTO toNotificationDetailDTO(Notification notification) {
        String formattedDate = notification.getDate().format(DateTimeFormatter.ofPattern("yy.MM.dd"));

        return NotificationResponseDTO.NotificationDetailDTO.builder()
                .notificationId(notification.getId())
                .date(formattedDate)
                .title(notification.getTitle())
                .content(notification.getContent())
                .build();
    }
}
