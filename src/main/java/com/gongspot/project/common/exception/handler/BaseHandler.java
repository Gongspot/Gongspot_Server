package com.gongspot.project.common.exception.handler;

import com.gongspot.project.common.code.BaseErrorCode;
import com.gongspot.project.common.exception.GeneralException;

public class BaseHandler extends GeneralException {

    public BaseHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}