package com.gongspot.project.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "공지사항 상세 응답 DTO")
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationDetailDTO {

        @Schema(description = "공지사항 ID", example = "1")
        private Long notificationId;

        @Schema(description = "작성일", example = "2025-08-04")
        private String date;

        @Schema(description = "공지 제목", example = "정기 점검 안내")
        private String title;

        @Schema(description = "공지 내용", example = "시스템 정기 점검으로 인해 ...")
        private String content;

        @Schema(description = "첨부파일 목록")
        private List<AttachmentDTO> attachments;
    }

    @Schema(description = "첨부파일 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttachmentDTO {

        @Schema(description = "첨부파일 ID", example = "101")
        private Long attachmentId;

        @Schema(description = "첨부파일 URL", example = "https://s3.aws.com/file123.pdf")
        private String url;

        @Schema(description = "파일 이름", example = "안내문.pdf")
        private String fileName;
    }
}
