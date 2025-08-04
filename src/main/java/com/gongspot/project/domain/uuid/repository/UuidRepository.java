package com.gongspot.project.domain.uuid.repository;

import com.gongspot.project.domain.uuid.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
