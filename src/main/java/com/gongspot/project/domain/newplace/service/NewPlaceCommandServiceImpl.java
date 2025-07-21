package com.gongspot.project.domain.newplace.service;

import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.entity.NewPlace;
import com.gongspot.project.domain.newplace.repository.NewPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewPlaceCommandServiceImpl implements NewPlaceCommandService {

    private final NewPlaceRepository newPlaceRepository;

    @Override
    @Transactional
    public NewPlaceResponseDTO createNewPlaceProposal(NewPlaceRequestDTO requestDTO) {
        NewPlace newPlace = new NewPlace(
                requestDTO.getName(),
                requestDTO.getLink(),
                requestDTO.getReason()
        );

        NewPlace saved = newPlaceRepository.save(newPlace);

        return NewPlaceResponseDTO.builder()
                .proposalId(saved.getId())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}