package com.gongspot.project.common.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private PageInfo pageInfo;
    private List<T> result;

    public static <T> PageResponse<T> of(Page<?> page, List<T> result) {
        return PageResponse.<T>builder()
                .pageInfo(PageInfo.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .build())
                .result(result)
                .build();
    }
}
