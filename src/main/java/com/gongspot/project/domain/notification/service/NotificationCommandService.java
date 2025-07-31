package com.gongspot.project.domain.notification.service;

import com.gongspot.project.domain.notification.dto.NotificationRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotificationCommandService {
    void createNotification(String category, NotificationRequestDTO requestDTO, List<MultipartFile> attachments );
}