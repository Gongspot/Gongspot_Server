package com.gongspot.project.global.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gongspot.project.domain.uuid.entity.Uuid;
import com.gongspot.project.domain.uuid.repository.UuidRepository;
import com.gongspot.project.global.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;


    public String uploadFile(String bucketName, String keyName, MultipartFile file, ObjectMetadata metadata) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        return amazonS3.getUrl(bucketName, keyName).toString();
    }

    public void deleteFile(String bucketName, String keyName) {
        try {
            amazonS3.deleteObject(bucketName, keyName);
            log.info("S3 파일 삭제 성공: {}/{}", bucketName, keyName);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}/{}", bucketName, keyName, e);
            throw new RuntimeException("S3 파일 삭제 실패", e);
        }
    }

    public String generateReviewKeyName(Uuid uuid) {
        return amazonConfig.getReviewBucket() + '/' + uuid.getUuid();
    }
}
