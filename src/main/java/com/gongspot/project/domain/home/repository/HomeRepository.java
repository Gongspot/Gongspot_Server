package com.gongspot.project.domain.home.repository;

import com.gongspot.project.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<Place, Long>, HomeRepositoryCustom {
}
