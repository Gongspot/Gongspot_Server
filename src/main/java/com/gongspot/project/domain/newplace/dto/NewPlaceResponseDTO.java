package com.gongspot.project.domain.newplace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.domain.newplace.entity.NewPlace;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public class NewPlaceResponseDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NewProposalDTO{
        private Long proposalId;
        private String name;
        private String link;
        private String reason;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NewProposalHomeDTO {
        private Integer totalAllProposalsCount;
        private Integer totalUnapprovedProposalsCount;
        private List<NewProposalDTO> unapprovedProposals;
    }
}