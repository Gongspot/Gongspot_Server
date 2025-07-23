package com.gongspot.project.domain.newplace.service;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;

public interface NewPlaceCommandService {
    NewPlaceResponseDTO createNewPlaceProposal(NewPlaceRequestDTO requestDTO);
    NewPlaceResponseDTO getProposal(Long proposalId);
    PageResponse<NewPlaceResponseDTO> getProposals(int page, int size);
}