package com.gongspot.project.domain.media.repository;

import com.gongspot.project.domain.media.entity.Media;
import com.gongspot.project.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    @Query("SELECT m FROM Media m WHERE m.place IN :places AND m.id IN (SELECT MIN(sub.id) FROM Media sub WHERE sub.place IN :places GROUP BY sub.place)")
    List<Media> findFirstMediaByPlaces(@Param("places") List<Place> places);
}
