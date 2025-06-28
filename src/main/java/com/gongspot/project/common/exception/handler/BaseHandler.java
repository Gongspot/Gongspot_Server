package com.gongspot.project.common.exception.handler;

import com.gongspot.project.common.code.BaseErrorCode;
import com.nimbusds.oauth2.sdk.GeneralException;

public class BaseHandler extends GeneralException {

    public BaseHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}