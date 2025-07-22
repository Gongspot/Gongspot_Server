package com.gongspot.project.domain.newplace.service;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.entity.NewPlace;
import com.gongspot.project.domain.newplace.repository.NewPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        return NewPlaceResponseDTO.fromBasic(saved);
    }

    @Override
    public NewPlaceResponseDTO getProposal(Long proposalId) {
        return newPlaceRepository.findById(proposalId)
                .map(NewPlaceResponseDTO::fromFull)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found"));
    }

    public PageResponse<NewPlaceResponseDTO> getProposals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewPlace> pageResult = newPlaceRepository.findAll(pageable);
        List<NewPlaceResponseDTO> dtoList = pageResult.getContent().stream()
                .map(NewPlaceResponseDTO::fromFull)
                .toList();

        return PageResponse.of(pageResult, dtoList);
    }
}