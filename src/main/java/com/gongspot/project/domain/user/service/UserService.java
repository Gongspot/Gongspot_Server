package com.gongspot.project.domain.user.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import com.gongspot.project.common.enums.RoleEnum;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                .role(RoleEnum.ROLE_USER)    
                .build();

        return userRepository.save(user);
    }

    public User findOrCreateUser(String email, String nickname, String profileImageUrl) {
        Optional<User> existing = userRepository.findByEmailAndDeletedAtIsNull(email);
        return existing.orElseGet(() -> createUser(email, nickname, profileImageUrl));
    }

    public void setPreferences(User user, UserRequestDTO.PreferRequestDTO prefDTO) {
        if (prefDTO.getPreferPlace().size() == 0 || prefDTO.getPreferPlace().size() > 3 ||
                prefDTO.getPurpose().size() == 0 || prefDTO.getPurpose().size() > 3 ||
                prefDTO.getLocation().size() == 0 || prefDTO.getLocation().size() > 3) {

            throw new GeneralException(ErrorStatus.INVALID_PREFERENCE);
        }

        user.updatePreferences(
                prefDTO.getPreferPlace(),
                prefDTO.getPurpose(),
                prefDTO.getLocation()
        );

        userRepository.save(user);
    }

}
