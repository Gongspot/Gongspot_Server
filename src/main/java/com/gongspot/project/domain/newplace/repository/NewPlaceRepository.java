package com.gongspot.project.domain.newplace.repository;

import com.gongspot.project.domain.newplace.entity.NewPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewPlaceRepository extends JpaRepository<NewPlace, Long> {
    Page<NewPlace> findAllByApprove(boolean approve, Pageable pageable);
    long countByApprove(boolean approve);
}