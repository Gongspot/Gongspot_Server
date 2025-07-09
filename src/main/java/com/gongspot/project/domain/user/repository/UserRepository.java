package com.gongspot.project.domain.user.repository;

import com.gongspot.project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
}
