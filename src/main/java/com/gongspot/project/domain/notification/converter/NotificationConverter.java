package com.gongspot.project.domain.notification.converter;

import com.gongspot.project.domain.banner.entity.Banner;
import com.gongspot.project.domain.notification.dto.NotificationRequestDTO;
import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;
import com.gongspot.project.domain.notification.entity.Notification;

import java.time.LocalDate;
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

    public static NotificationResponseDTO.NotificationDetailDTO toNotificationDetailDTO(Notification notification , List<NotificationResponseDTO.AttachmentDTO> attachments) {
        String formattedDate = notification.getDate().format(DateTimeFormatter.ofPattern("yy.MM.dd"));

        return NotificationResponseDTO.NotificationDetailDTO.builder()
                .notificationId(notification.getId())
                .date(formattedDate)
                .title(notification.getTitle())
                .content(notification.getContent())
                .attachments(attachments)
                .build();
    }

    public static Notification toNotificationEntity(NotificationRequestDTO requestDTO){
        return Notification.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .date(LocalDate.now())
                .build();
    }

    public static Banner toBannerEntity(NotificationRequestDTO requestDTO) {
        return Banner.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .date(LocalDate.now())
                .build();
    }

    public static void updateNotificationEntity(Notification notification, NotificationRequestDTO requestDTO) {
        notification.setTitle(requestDTO.getTitle());
        notification.setContent(requestDTO.getContent());
    }

    public static void updateBannerEntity(Banner banner, NotificationRequestDTO requestDTO) {
        banner.setTitle(requestDTO.getTitle());
        banner.setContent(requestDTO.getContent());
    }

    public static NotificationResponseDTO.NotificationBannerItemDTO toNotificationBannerItem(Notification notification) {
        String formattedDate = notification.getDate().format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        return NotificationResponseDTO.NotificationBannerItemDTO.builder()
                .type("N")
                .notificationId(notification.getId())
                .date(formattedDate)
                .title(notification.getTitle())
                .build();
    }

    public static NotificationResponseDTO.NotificationBannerItemDTO toNotificationBannerItem(Banner banner) {
        String formattedDate = banner.getDate().format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        return NotificationResponseDTO.NotificationBannerItemDTO.builder()
                .type("B")
                .bannerId(banner.getId())
                .date(formattedDate)
                .title(banner.getTitle())
                .build();
    }

    public static NotificationResponseDTO.NotificationBannerListDTO toNotificationBannerListDTO(List<NotificationResponseDTO.NotificationBannerItemDTO> notificationBannerItems) {
        return NotificationResponseDTO.NotificationBannerListDTO.builder()
                .notificationBannerList(notificationBannerItems)
                .build();
    }
}