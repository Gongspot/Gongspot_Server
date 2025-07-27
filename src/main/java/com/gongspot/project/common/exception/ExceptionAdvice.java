package com.gongspot.project.common.exception;

import com.gongspot.project.common.code.ErrorReasonDTO;
import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("ConstraintViolationException 처리 중 오류 발생");

        ErrorStatus errorStatus = ErrorStatus.valueOf(errorMessage);
        return buildResponseEntity(e, errorStatus, HttpHeaders.EMPTY, request, null);
    }

    // DTO 유효성 관련 예외
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
            errors.merge(fieldName, errorMessage, (existing, newMsg) -> existing + ", " + newMsg);
        });

        ErrorStatus errorStatus = ErrorStatus._BAD_REQUEST;
        return buildResponseEntity(e, errorStatus, HttpHeaders.EMPTY, request, errors);
    }

    // 예외 최종적 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e, WebRequest request) {
        log.error("Unhandled exception occurred", e);
        return buildResponseEntity(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, request, e.getMessage());
    }

    // 커스텀 예외 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException e, HttpServletRequest request) {
        ErrorReasonDTO reason = e.getErrorReasonHttpStatus();
        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);
        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(e, body, null, reason.getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> buildResponseEntity(Exception e, ErrorStatus status,
                                                       HttpHeaders headers, WebRequest request, Object errorData) {
        ApiResponse<Object> body = ApiResponse.onFailure(status.getCode(), status.getMessage(), errorData);
        return super.handleExceptionInternal(e, body, headers, status.getHttpStatus(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        log.warn("AccessDeniedException: {}", e.getMessage());
        return buildResponseEntity(e, ErrorStatus._FORBIDDEN, HttpHeaders.EMPTY, request, null);
    }
}
