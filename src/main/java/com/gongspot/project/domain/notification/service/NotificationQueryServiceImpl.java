package com.gongspot.project.domain.notification.service;

import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.banner.entity.Banner;
import com.gongspot.project.domain.banner.repository.BannerRepository;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.notification.converter.NotificationConverter;
import com.gongspot.project.domain.notification.dto.NotificationResponseDTO;
import com.gongspot.project.domain.notification.entity.Notification;
import com.gongspot.project.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gongspot.project.common.code.status.ErrorStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService{
    private final NotificationRepository notificationRepository;
    private final MediaRepository mediaRepository;
    private final BannerRepository bannerRepository;

    @Override
    public NotificationResponseDTO.NotificationListDTO getNotificationList() {
        List<Notification> notificationList = notificationRepository.findAllByOrderByCreatedAtDesc();

        return NotificationConverter.toNotificationListResultDTO(notificationList);
    }

    @Override
    public NotificationResponseDTO.NotificationDetailDTO getNotificationDetail(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        List<Media> attachments = mediaRepository.findByNotificationId(notificationId);
        List<NotificationResponseDTO.AttachmentDTO> attachmentDTOs = attachments.stream()
                .map(media -> NotificationResponseDTO.AttachmentDTO.builder()
                        .attachmentId(media.getId())
                        .url(media.getUrl())
                        .fileName(media.getOriginalFileName())
                        .build())
                .collect(Collectors.toList());
        return NotificationConverter.toNotificationDetailDTO(notification,attachmentDTOs);
    }
    @Override
    public NotificationResponseDTO.NotificationBannerListDTO getNotificationBannerList(String type) {
        List<NotificationResponseDTO.NotificationBannerItemDTO> combinedList = new ArrayList<>();

        if (type == null || type.equalsIgnoreCase("ALL") || type.equalsIgnoreCase("N")) {
            List<Notification> notifications = notificationRepository.findAllByOrderByCreatedAtDesc();
            combinedList.addAll(notifications.stream()
                    .map(NotificationConverter::toNotificationBannerItem)
                    .collect(Collectors.toList()));
        }

        if (type == null || type.equalsIgnoreCase("ALL") || type.equalsIgnoreCase("B")) {
            List<Banner> banners = bannerRepository.findAll();
            combinedList.addAll(banners.stream()
                    .map(NotificationConverter::toNotificationBannerItem)
                    .collect(Collectors.toList()));
        }

        if (type == null || type.equalsIgnoreCase("ALL")) {
            combinedList.sort(Comparator.comparing(NotificationResponseDTO.NotificationBannerItemDTO::getDate).reversed()); // 최신순 정렬
        }

        return NotificationConverter.toNotificationBannerListDTO(combinedList);
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
