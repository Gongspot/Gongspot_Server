package com.gongspot.project.domain.newplace.repository;

import com.gongspot.project.domain.newplace.entity.NewPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

public interface NewPlaceRepository extends JpaRepository<NewPlace, Long> {
    Page<NewPlace> findAllByDeletedFalseAndApprove(boolean approve, Pageable pageable);
    Page<NewPlace> findAllByDeletedFalse(Pageable pageable);
    long countByDeletedFalseAndApprove(boolean approve);
    long countByDeletedFalse();
}