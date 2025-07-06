package com.gongspot.project.common.exception;

import com.gongspot.project.common.code.ErrorReasonDTO;
import com.gongspot.project.common.code.status.ErrorStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public BusinessException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorReasonDTO getReason() {
        return this.errorStatus.getReasonHttpStatus();
    }
}
