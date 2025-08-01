package com.gongspot.project.domain.newplace.service;

import com.gongspot.project.common.code.PageResponse;
import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.newplace.dto.NewPlaceRequestDTO;
import com.gongspot.project.domain.newplace.dto.NewPlaceResponseDTO;
import com.gongspot.project.domain.newplace.entity.NewPlace;
import com.gongspot.project.domain.newplace.repository.NewPlaceRepository;
import com.gongspot.project.domain.place.converter.PlaceConverter;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewPlaceCommandServiceImpl implements NewPlaceCommandService {

    private final NewPlaceRepository newPlaceRepository;
    private final PlaceConverter placeConverter;
    private final PlaceRepository placeRepository;

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

    public PageResponse<NewPlaceResponseDTO> getProposals(Boolean approve, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewPlace> pageResult;

        if (approve == null) {
            pageResult = newPlaceRepository.findAll(pageable); // 전체 목록
        } else {
            pageResult = newPlaceRepository.findAllByApprove(approve, pageable); // 필터링된 목록
        }

        List<NewPlaceResponseDTO> dtoList = pageResult.getContent().stream()
                .map(NewPlaceResponseDTO::fromFull)
                .toList();

        return PageResponse.of(pageResult, dtoList);
    }

    @Override
    @Transactional
    public void approveProposal(Long proposalId, PlaceResponseDTO.PlaceApprovalRequestDTO requestDTO) {
        NewPlace newPlace = newPlaceRepository.findById(proposalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND));

        newPlace.setApprove(true);
        newPlaceRepository.save(newPlace);

        Place place = placeConverter.convertToPlaceEntity(requestDTO);
        placeRepository.save(place);
    }

}