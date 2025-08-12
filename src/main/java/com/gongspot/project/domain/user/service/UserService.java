package com.gongspot.project.domain.user.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.enums.LocationEnum;
import com.gongspot.project.common.enums.PlaceEnum;
import com.gongspot.project.common.enums.PurposeEnum;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import com.gongspot.project.common.enums.RoleEnum;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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

    @Transactional
    public void updatePreferences(User givenuser, UserRequestDTO.PreferRequestDTO request) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(givenuser.getEmail())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (request == null) {
            throw new GeneralException(ErrorStatus.NO_CHANGES);
        }

        List<PlaceEnum> existingPreferPlace = user.getPreferPlace() != null ? user.getPreferPlace() : Collections.emptyList();
        List<PurposeEnum> existingPurpose = user.getPurpose() != null ? user.getPurpose() : Collections.emptyList();
        List<LocationEnum> existingLocation = user.getLocation() != null ? user.getLocation() : Collections.emptyList();

        List<PlaceEnum> newPreferPlace = request.getPreferPlace() != null ? request.getPreferPlace() : existingPreferPlace;
        List<PurposeEnum> newPurpose = request.getPurpose() != null ? request.getPurpose() : existingPurpose;
        List<LocationEnum> newLocation = request.getLocation() != null ? request.getLocation() : existingLocation;

        if (newPreferPlace.equals(existingPreferPlace) &&
                newPurpose.equals(existingPurpose) &&
                newLocation.equals(existingLocation)) {

            throw new GeneralException(ErrorStatus.NO_CHANGES);
        }

        if (request.getPreferPlace() != null) {
            user.updatePreferPlace(request.getPreferPlace());
        }
        if (request.getPurpose() != null) {
            user.updatePurpose(request.getPurpose());
        }
        if (request.getLocation() != null) {
            user.updateLocation(request.getLocation());
        }

        userRepository.save(user);
    }
}
