package com.gongspot.project.domain.user.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public User createUser(String email, String nickname, String profileImageUrl) {
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .profileImg(profileImageUrl)
                .build();

        return userRepository.save(user);
    }


}
