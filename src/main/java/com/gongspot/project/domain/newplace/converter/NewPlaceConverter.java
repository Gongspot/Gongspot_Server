package com.gongspot.project.domain.newplace.converter;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.entity.NewPlace;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewPlaceConverter {

    public NewPlaceResponseDTO.NewProposalDTO toBasicNewPlaceResponseDTO(NewPlace newPlace) {
        return NewPlaceResponseDTO.NewProposalDTO.builder()
                .proposalId(newPlace.getId())
                .createdAt(newPlace.getCreatedAt())
                .build();
    }

    public NewPlaceResponseDTO.NewProposalDTO toFullNewPlaceResponseDTO(NewPlace newPlace) {
        return NewPlaceResponseDTO.NewProposalDTO.builder()
                .proposalId(newPlace.getId())
                .name(newPlace.getName())
                .link(newPlace.getLink())
                .reason(newPlace.getReason())
                .createdAt(newPlace.getCreatedAt())
                .build();
    }

    public NewPlaceResponseDTO.NewProposalHomeDTO toNewProposalHomeDTO(
            Page<NewPlace> unapprovedPage,
            long totalAllProposalsCount,
            long totalUnapprovedProposalsCount
    ) {
        List<NewPlaceResponseDTO.NewProposalDTO> dtoList = unapprovedPage.getContent().stream()
                .map(this::toFullNewPlaceResponseDTO)                 .toList();

        return NewPlaceResponseDTO.NewProposalHomeDTO.builder()
                .totalAllProposalsCount(totalAllProposalsCount)
                .totalUnapprovedProposalsCount(totalUnapprovedProposalsCount)
                .unapprovedProposals(dtoList)
                .build();
    }
}
