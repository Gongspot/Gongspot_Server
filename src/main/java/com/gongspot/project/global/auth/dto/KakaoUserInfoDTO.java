package com.gongspot.project.global.auth.dto;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class KakaoUserInfoDTO {
    private String email;
    private String nickname;
    private String profileImageUrl;

    public static KakaoUserInfoDTO from(Map<String, Object> kakaoUserInfo) {
        if (kakaoUserInfo == null || !(kakaoUserInfo.get("kakao_account") instanceof Map)) {
            throw new GeneralException(ErrorStatus.OAUTH_INVALID_KAKAO_USERINFO);
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");

        String email = kakaoAccount.get("email") instanceof String ? (String) kakaoAccount.get("email") : null;
        if (email == null) {
            throw new GeneralException(ErrorStatus.OAUTH_KAKAO_EMAIL_NOT_FOUND);
        }

        if (!(kakaoAccount.get("profile") instanceof Map)) {
            throw new GeneralException(ErrorStatus.OAUTH_KAKAO_PROFILE_NOT_FOUND);
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = profile.get("nickname") instanceof String ? (String) profile.get("nickname") : null;
        String profileImageUrl = profile.get("profile_image_url") instanceof String ? (String) profile.get("profile_image_url") : null;

        if (nickname == null || profileImageUrl == null) {
            throw new GeneralException(ErrorStatus.OAUTH_KAKAO_PROFILE_DETAIL_MISSING);
        }

        KakaoUserInfoDTO dto = new KakaoUserInfoDTO();
        dto.setEmail(email);
        dto.setNickname(nickname);
        dto.setProfileImageUrl(profileImageUrl);
        return dto;
    }
}
