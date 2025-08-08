package com.gongspot.project.domain.banner.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.banner.converter.BannerConverter;
import com.gongspot.project.domain.banner.dto.BannerRequestDTO;
import com.gongspot.project.domain.banner.entity.Banner;
import com.gongspot.project.domain.banner.repository.BannerRepository;
import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.media.repository.MediaRepository;
import com.gongspot.project.domain.uuid.repository.UuidRepository;
import com.gongspot.project.domain.uuid.entity.Uuid;
import com.gongspot.project.global.aws.s3.AmazonS3Manager;
import com.gongspot.project.global.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BannerCommandServiceImpl implements BannerCommandService {
    private final BannerRepository bannerRepository;
    private final MediaRepository mediaRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final AmazonConfig amazonConfig;

    @Override
    @Transactional
    public void updateBanner(Long bannerId, BannerRequestDTO bannerRequestDTO, MultipartFile thumbnailFile, List<Long> mediaIdsToDelete, List<MultipartFile> attachments) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new BusinessException(ErrorStatus.BANNER_NOT_FOUND));

        BannerConverter.updateBannerEntity(banner, bannerRequestDTO);
        bannerRepository.save(banner);

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            mediaRepository.findByBannerAndIsThumbnail(banner, true).ifPresent(media -> {
                String keyName = media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1);
                s3Manager.deleteFile(amazonConfig.getNotificationBucket(), keyName);
                mediaRepository.delete(media);
            });
            String url = uploadToS3(thumbnailFile);
            Media thumbnailMedia = createMedia(url, thumbnailFile.getOriginalFilename(),
                    thumbnailFile.getContentType(), true);
            thumbnailMedia.setBanner(banner);
            mediaRepository.save(thumbnailMedia);
        }
        if (mediaIdsToDelete != null && !mediaIdsToDelete.isEmpty()) {
            List<Media> mediaToDelete = mediaRepository.findAllById(mediaIdsToDelete);

            for (Media media : mediaToDelete) {
                if (media.getBanner() != null && media.getBanner().getId().equals(bannerId)) {
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
                media.setBanner(banner);
                mediaRepository.save(media);
            }
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
