package com.gongspot.project.domain.newplace.service;

import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;

public interface NewPlaceCommandService {
    NewPlaceResponseDTO createNewPlaceProposal(NewPlaceRequestDTO requestDTO);
}