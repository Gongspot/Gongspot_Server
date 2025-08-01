package com.gongspot.project.domain.newplace.service;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;

public interface NewPlaceCommandService {
    NewPlaceResponseDTO createNewPlaceProposal(NewPlaceRequestDTO requestDTO);
    NewPlaceResponseDTO getProposal(Long proposalId);
    PageResponse<NewPlaceResponseDTO> getProposals(Boolean approve, int page, int size);
    void approveProposal(Long proposalId, PlaceResponseDTO.PlaceApprovalRequestDTO requestDTO);
}