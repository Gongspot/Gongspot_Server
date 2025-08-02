package com.gongspot.project.domain.notification.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class NotificationResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationItemDTO {
        private Long notificationId;
        private String date;
        private String title;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationBannerItemDTO {
        private String type;
        private Long bannerId;
        private Long notificationId;
        private String date;
        private String title;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationListDTO {
        private List<NotificationItemDTO> notificationList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationBannerListDTO {
        private List<NotificationBannerItemDTO> notificationBannerList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationDetailDTO {
        private Long notificationId;
        private String date;
        private String title;
        private String content;
        private List<AttachmentDTO> attachments;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttachmentDTO {
        private Long attachmentId;
        private String url;
        private String fileName;
    }
}
