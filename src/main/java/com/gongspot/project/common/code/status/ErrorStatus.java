package com.gongspot.project.common.code.status;

import com.gongspot.project.common.code.BaseErrorCode;
import com.gongspot.project.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "COMMON405", "요청 JSON 형식이 잘못되었습니다."),

    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "테스트용 예외입니다."),

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER4003", "이미 존재하는 닉네임입니다."),

    // 인증 관련 에러
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH4001", "유효하지 않거나 만료된 토큰입니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH4002", "유효하지 않은 리프레시 토큰입니다."),

    // 카카오 관련 에러
    KAKAO_TOKEN_PARSE_FAILED(HttpStatus.BAD_REQUEST, "KAKAO4001", "카카오 토큰 파싱에 실패했습니다."),
    KAKAO_USERINFO_PARSE_FAILED(HttpStatus.BAD_REQUEST, "KAKAO4002", "카카오 사용자 정보 파싱에 실패했습니다."),
    KAKAO_TOKEN_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "KAKAO4003", "카카오 토큰 요청에 실패했습니다."),

    // OAuth 구조 관련 에러
    OAUTH_INVALID_KAKAO_USERINFO(HttpStatus.BAD_REQUEST, "OAUTH4001", "카카오 사용자 정보가 올바르지 않습니다."),
    OAUTH_KAKAO_EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "OAUTH4002", "카카오 계정의 이메일 정보를 찾을 수 없습니다."),
    OAUTH_KAKAO_PROFILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "OAUTH4003", "카카오 계정의 프로필 정보를 찾을 수 없습니다."),
    OAUTH_KAKAO_PROFILE_DETAIL_MISSING(HttpStatus.BAD_REQUEST, "OAUTH4004", "카카오 프로필 정보 중 nickname 또는 이미지가 없습니다."),


    // Place Error
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE4001", "공간을 찾을 수 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND,"PLACE4002","해당 공간에 찜한 기록이 없습니다." ),
    PLACE_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE4003", "공간 카테고리를 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "PLACE4004", "shortUrl이나 name 중 하나는 제공되어야 합니다."),

    // Review Error
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW4001", "리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "REVIEW4002", "이미 해당 장소에 리뷰를 작성했습니다."),
    REVIEW_SAVE_FAIL(HttpStatus.BAD_REQUEST, "REVIEW4003", "리뷰 저장에 실패했습니다."),

    // Media Error
    MEDIA_NOT_FOUND(HttpStatus.NOT_FOUND, "MEDIA4001", "미디어를 찾을 수 없습니다."),

    // Notification Error
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION4001", "공지사항을 찾을 수 없습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "NOTIFICATION4002", "잘못된 카테고리입니다."),

    // Banner Error
    BANNER_NOT_FOUND(HttpStatus.NOT_FOUND, "BANNER4001", "배너를 찾을 수 없습니다."),

    // 파일 업로드 관련 에러
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE4001", "파일 업로드 중 오류가 발생했습니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "FILE4002", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE4003", "파일 크기가 제한을 초과했습니다.");
    ; // 위에 적을 것 !

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}