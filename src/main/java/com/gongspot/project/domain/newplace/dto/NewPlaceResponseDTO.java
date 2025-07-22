package com.gongspot.project.domain.newplace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongspot.project.domain.newplace.entity.NewPlace;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewPlaceResponseDTO {
    private Long proposalId;
    private String name;
    private String link;
    private String reason;
    private LocalDateTime createdAt;

    public static NewPlaceResponseDTO fromBasic(NewPlace newPlace) {
        return NewPlaceResponseDTO.builder()
                .proposalId(newPlace.getId())
                .createdAt(newPlace.getCreatedAt())
                .build();
    }

    public static NewPlaceResponseDTO fromFull(NewPlace newPlace) {
        return NewPlaceResponseDTO.builder()
                .proposalId(newPlace.getId())
                .name(newPlace.getName())
                .link(newPlace.getLink())
                .reason(newPlace.getReason())
                .createdAt(newPlace.getCreatedAt())
                .build();
    }

}