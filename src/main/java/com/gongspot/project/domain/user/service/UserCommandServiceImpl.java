package com.gongspot.project.domain.user.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.global.auth.service.S3UploadService;
import com.gongspot.project.domain.user.converter.UserConverter;
import com.gongspot.project.domain.user.dto.UserRequestDTO;
import com.gongspot.project.domain.user.dto.UserResponseDTO;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;

    @Override
    @Transactional
    public void registerNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (nickname == null || nickname.trim().isEmpty()) {
            throw new GeneralException(ErrorStatus.NICKNAME_NOT_EXIST);
        }

        user.updateNickname(nickname.trim());
    }

    @Override
    @Transactional
    public UserResponseDTO.ProfileResponseDTO updateProfile(Long userId, UserRequestDTO.ProfileRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        String nickname = request.getNickname();
        String newProfileImgUrl = user.getProfileImg(); // 기존 이미지 URL 유지
        String oldImageUrl = null;

        // 닉네임 처리 및 중복 체크
        if (nickname != null) {
            nickname = nickname.trim();
            // 닉네임이 변경된 경우에만 중복 체크
            if (!nickname.equals(user.getNickname()) && userRepository.existsByNickname(nickname)) {
                throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
            }
        } else {
            // 닉네임이 null이면 기존 닉네임 유지
            nickname = user.getNickname();
        }

        // 프로필 이미지 업로드 처리
        if (request.getProfileImg() != null && !request.getProfileImg().isEmpty()) {

            oldImageUrl = user.getProfileImg(); // 삭제용 기존 이미지 URL 보관

            try {
                // 새 이미지를 S3에 업로드하고 새로운 URL을 받아옵니다.
                newProfileImgUrl = s3UploadService.uploadFile(request.getProfileImg());
            } catch (IOException e) {
                log.error("파일 업로드 실패: userId={}, error={}", userId, e.getMessage(), e);
                throw new GeneralException(ErrorStatus.FILE_UPLOAD_ERROR);
            }
        }

        // 사용자 정보 업데이트
        user.updateProfile(nickname, newProfileImgUrl);

        // 트랜잭션 완료 후 기존 이미지 삭제 (새 이미지가 업로드된 경우만)
        if (oldImageUrl != null) {
            // (참고) S3UploadService의 deleteFile 내부에서 우리 S3 버킷의 파일인지 검사하므로
            // 카카오 프로필 같은 외부 URL은 삭제 시도조차 하지 않아 안전합니다.
            s3UploadService.deleteFile(oldImageUrl);
        }

        return UserConverter.toProfileResponseDTO(user);
    }
}