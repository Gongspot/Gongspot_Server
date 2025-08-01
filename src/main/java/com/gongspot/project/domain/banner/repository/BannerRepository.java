package com.gongspot.project.domain.banner.repository;

import com.gongspot.project.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerRepositoryCustom {
}
