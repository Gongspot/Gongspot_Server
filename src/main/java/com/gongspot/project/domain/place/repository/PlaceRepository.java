package com.gongspot.project.domain.place.repository;

import com.gongspot.project.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findById(Long placeId);
}
