package com.gongspot.project.domain.banner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerRequestDTO {
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    private String content;
}