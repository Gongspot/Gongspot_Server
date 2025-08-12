package com.gongspot.project.global.auth.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gongspot.project.global.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {

    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;

    public String uploadFile(MultipartFile file) throws IOException {
        log.debug("사용할 버킷: {}", amazonConfig.getProfileBucket());

        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 파일 확장자 검증
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
        }

        String extension = getFileExtension(originalFileName);
        if (!isValidImageExtension(extension)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 지원)");
        }

        // 고유한 파일명 생성
        String fileName = "profile/" + UUID.randomUUID().toString() + extension;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 업로드 (프로필 버킷 사용)
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                amazonConfig.getProfileBucket(),
                fileName,
                file.getInputStream(),
                metadata
        );

        amazonS3.putObject(putObjectRequest);

        // 업로드된 파일의 URL 반환
        return amazonS3.getUrl(amazonConfig.getProfileBucket(), fileName).toString();
    }

    public void deleteFile(String fileUrl) {
        try {
            // 외부 URL (카카오 CDN 등)은 삭제하지 않음
            if (fileUrl == null || fileUrl.isEmpty() ||
                    !fileUrl.contains(amazonConfig.getProfileBucket())) {
                log.debug("외부 URL이거나 빈 URL입니다. 삭제하지 않습니다: {}", fileUrl);
                return;
            }

            // URL에서 파일명 추출
            String fileName = extractFileNameFromUrl(fileUrl);
            if (fileName != null) {
                amazonS3.deleteObject(amazonConfig.getProfileBucket(), fileName);
                log.debug("파일 삭제 성공: {}", fileName);
            }
        } catch (Exception e) {
            // 삭제 실패해도 로그만 남기고 진행 (기존 파일이 없을 수도 있음)
            log.warn("파일 삭제 실패: {}, 에러: {}", fileUrl, e.getMessage());
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        try {
            // URL 디코딩
            String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);

            // S3 URL에서 파일명 추출
            // 예: https://bucket-name.s3.region.amazonaws.com/profile/uuid.jpg
            String bucketUrl = "https://" + amazonConfig.getProfileBucket() + ".s3.";
            int bucketIndex = decodedUrl.indexOf(bucketUrl);

            if (bucketIndex != -1) {
                // 버킷 URL 이후의 파일 경로 추출
                int pathStart = decodedUrl.indexOf("/", bucketIndex + bucketUrl.length());
                if (pathStart != -1) {
                    String filePath = decodedUrl.substring(pathStart + 1);
                    // 쿼리 파라미터 제거 (있다면)
                    int queryIndex = filePath.indexOf("?");
                    if (queryIndex != -1) {
                        filePath = filePath.substring(0, queryIndex);
                    }
                    return filePath;
                }
            }

            // fallback: 마지막 두 경로 세그먼트 추출
            String[] parts = decodedUrl.split("/");
            if (parts.length >= 2) {
                return parts[parts.length - 2] + "/" + parts[parts.length - 1];
            }

            // 최종 fallback
            return decodedUrl.substring(decodedUrl.lastIndexOf("/") + 1);

        } catch (Exception e) {
            log.error("URL에서 파일명 추출 실패: {}", fileUrl, e);
            return null;
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex);
    }

    private boolean isValidImageExtension(String extension) {
        return extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg") ||
                extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".gif");
    }
}