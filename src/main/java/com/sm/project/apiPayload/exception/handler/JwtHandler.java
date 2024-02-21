package com.sm.project.apiPayload.exception.handler;

import com.sm.project.apiPayload.code.BaseErrorCode;
import com.sm.project.apiPayload.exception.GeneralException;

public class JwtHandler extends GeneralException {
    public JwtHandler(BaseErrorCode code) {
        super(code);
    }
}
