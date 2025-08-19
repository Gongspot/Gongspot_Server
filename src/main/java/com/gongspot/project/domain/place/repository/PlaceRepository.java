package com.gongspot.project.domain.place.repository;

import com.gongspot.project.common.enums.*;
import com.gongspot.project.domain.place.dto.PlaceRequestDTO;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.place.entity.QPlace;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {
    Optional<Place> findById(Long placeId);
}
