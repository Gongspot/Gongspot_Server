package com.gongspot.project.domain.newplace.repository;

import com.gongspot.project.domain.newplace.entity.NewPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewPlaceRepository extends JpaRepository<NewPlace, Long> {
}