package com.gongspot.project.domain.point.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfo {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}