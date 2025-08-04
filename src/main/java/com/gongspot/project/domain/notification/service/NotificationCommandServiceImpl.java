package com.gongspot.project.domain.notification.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.domain.uuid.entity.Uuid;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.banner.entity.Banner;
import com.gongspot.project.domain.banner.repository.BannerRepository;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.notification.dto.NotificationRequestDTO;
import com.gongspot.project.domain.notification.entity.Notification;
import com.gongspot.project.domain.notification.repository.NotificationRepository;
import com.gongspot.project.domain.uuid.repository.UuidRepository;
import com.gongspot.project.global.aws.s3.AmazonS3Manager;
import com.gongspot.project.global.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gongspot.project.domain.notification.converter.NotificationConverter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService{

    private final BannerRepository bannerRepository;
    private final NotificationRepository notificationRepository;
    private final MediaRepository mediaRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final AmazonConfig amazonConfig;

    @Override
    @Transactional
    public void createNotification(String category, NotificationRequestDTO requestDTO, List<MultipartFile> attachments, MultipartFile thumbnailFile) {
        validateCategory(category);

        if (category.equals("B")) {
            Banner banner = NotificationConverter.toBannerEntity(requestDTO);
            bannerRepository.save(banner);

            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                String url = uploadToS3(thumbnailFile);
                Media thumbnailMedia = createMedia(url, thumbnailFile.getOriginalFilename(), thumbnailFile.getContentType(), true);
                thumbnailMedia.setBanner(banner);
                mediaRepository.save(thumbnailMedia);
            }

            if (attachments != null) {
                for (MultipartFile file : attachments) {
                    if (file == null || file.isEmpty()) continue;
                    String url = uploadToS3(file);
                    Media media = createMedia(url, file.getOriginalFilename(), file.getContentType(), false);
                    media.setBanner(banner);
                    mediaRepository.save(media);
                }
            }

        } else if (category.equals("N")) {
            Notification notification = NotificationConverter.toNotificationEntity(requestDTO);
            notification = notificationRepository.save(notification);

            if (attachments != null) {
                for (MultipartFile file : attachments) {
                    if (file == null || file.isEmpty()) continue;
                    String url = uploadToS3(file);
                    Media media = createMedia(url, file.getOriginalFilename(), file.getContentType(), false);
                    media.setNotification(notification);
                    mediaRepository.save(media);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateNotification(Long notificationId, NotificationRequestDTO requestDTO,List<Long> mediaIdsToDelete, List<MultipartFile> attachments) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        NotificationConverter.updateNotificationEntity(notification, requestDTO);
        notificationRepository.save(notification);

        if (mediaIdsToDelete != null && !mediaIdsToDelete.isEmpty()) {
            List<Media> mediaToDelete = mediaRepository.findAllById(mediaIdsToDelete);

            for (Media media : mediaToDelete) {
                if (media.getNotification() != null && media.getNotification().getId().equals(notificationId)) {
                    String keyName = media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1);
                    s3Manager.deleteFile(amazonConfig.getNotificationBucket(), keyName);
                    mediaRepository.delete(media);
                }
            }
        }

        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if (file == null || file.isEmpty()) continue;

                String url = uploadToS3(file);
                Media media = createMedia(url, file.getOriginalFilename(), file.getContentType(), false);
                media.setNotification(notification);
                mediaRepository.save(media);
            }
        }
    }

    private void validateCategory(String category) {
        if (category == null || (!category.equals("B") && !category.equals("N"))) {
            throw new BusinessException(ErrorStatus.INVALID_CATEGORY);
        }
    }

    private String uploadToS3(MultipartFile file) {
        String uuidStr = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuidStr).build());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        return s3Manager.uploadFile(amazonConfig.getNotificationBucket(), savedUuid.getUuid(), file, metadata);
    }

    private Media createMedia(String url, String originalFileName, String contentType, boolean isThumbnail) {
        Media media = new Media();
        media.setUrl(url);
        media.setOriginalFileName(originalFileName);
        media.setContentType(contentType);
        media.setIsThumbnail(isThumbnail);
        return media;
    }
}