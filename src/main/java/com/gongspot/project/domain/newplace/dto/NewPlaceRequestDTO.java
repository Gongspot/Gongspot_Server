package com.gongspot.project.domain.newplace.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewPlaceRequestDTO {
    private String name;
    private String link;
    private String reason;
}
