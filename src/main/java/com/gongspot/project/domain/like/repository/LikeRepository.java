package com.gongspot.project.domain.like.repository;

import com.gongspot.project.domain.like.entity.Like;
import com.gongspot.project.domain.place.entity.Place;
import com.gongspot.project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPlace(User user, Place place);
    boolean existsByUserAndPlace(User user, Place place);
    List<Like> findAllByUser(User user);
    List<Like> findAllByUserAndPlaceIsFreeTrue(User user);
    List<Like> findAllByUserAndPlaceIsFreeFalse(User user);
}
