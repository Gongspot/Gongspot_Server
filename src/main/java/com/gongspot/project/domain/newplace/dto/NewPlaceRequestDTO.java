package com.gongspot.project.domain.newplace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPlaceRequestDTO {
    private String name;
    private String link;
    private String reason;
}