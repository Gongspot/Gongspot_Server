package com.gongspot.project.domain.user.repository;

import com.gongspot.project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
}
