package com.gongspot.project.domain.notification.service;

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
    public void createNotification(String category, NotificationRequestDTO requestDTO, List<MultipartFile> attachments) {
        validateCategory(category);

        if (category.equals("B")) {
            Banner banner = NotificationConverter.toBannerEntity(requestDTO);
            bannerRepository.save(banner);

            if (attachments != null) {
                for (MultipartFile file : attachments) {
                    String uuidStr = UUID.randomUUID().toString();
                    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuidStr).build());

                    String keyName = savedUuid.getUuid();
                    String url = s3Manager.uploadFile(amazonConfig.getNotificationBucket(), keyName, file);

                    Media media = new Media();
                    media.setBanner(banner);
                    media.setUrl(url);
                    mediaRepository.save(media);
                }
            }

        } else if (category.equals("N")) {
            Notification notification = NotificationConverter.toNotificationEntity(requestDTO);
            notificationRepository.save(notification);

            if (attachments != null) {
                for (MultipartFile file : attachments) {
                    if (file == null || file.isEmpty()) continue;
                    String uuidStr = UUID.randomUUID().toString();
                    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuidStr).build());

                    String keyName = savedUuid.getUuid();
                    String url = s3Manager.uploadFile(amazonConfig.getNotificationBucket(), keyName, file);

                    Media media = new Media();
                    media.setNotification(notification);
                    media.setUrl(url);
                    mediaRepository.save(media);
                }
            }
        }
    }

    private void validateCategory(String category) {
        if (category == null || (!category.equals("B") && !category.equals("N"))) {
            throw new BusinessException(ErrorStatus.INVALID_CATEGORY);
        }
    }
}
