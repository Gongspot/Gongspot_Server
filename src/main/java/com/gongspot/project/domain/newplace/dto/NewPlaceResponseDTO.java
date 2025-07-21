package com.gongspot.project.domain.newplace.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewPlaceResponseDTO {
    private Long proposalId;
    private LocalDateTime createdAt;
}