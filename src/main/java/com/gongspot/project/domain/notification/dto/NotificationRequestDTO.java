package com.gongspot.project.domain.notification.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO{
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    private String content;
}
