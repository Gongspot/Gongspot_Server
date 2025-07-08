package com.gongspot.project.common.exception;

import com.gongspot.project.common.code.BaseErrorCode;
import com.gongspot.project.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }

    public GeneralException(BaseErrorCode code) {
        super(code.getReason().getMessage()); // 메시지 들어가도록 생성자 추가
        this.code = code;
    }
}