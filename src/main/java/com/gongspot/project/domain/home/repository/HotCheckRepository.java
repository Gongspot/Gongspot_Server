package com.gongspot.project.domain.home.repository;

import com.gongspot.project.domain.home.entity.HotCheck;
import com.gongspot.project.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotCheckRepository extends JpaRepository<HotCheck, Long> {
    Optional<HotCheck> findByPlaceAndWeek(Place place, Integer week);
}
