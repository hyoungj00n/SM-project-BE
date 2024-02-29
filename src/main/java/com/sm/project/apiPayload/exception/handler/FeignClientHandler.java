package com.sm.project.apiPayload.exception.handler;

import com.sm.project.apiPayload.code.BaseErrorCode;
import com.sm.project.apiPayload.exception.GeneralException;

public class FeignClientHandler extends GeneralException {
    public FeignClientHandler(BaseErrorCode code) {
        super(code);
    }
}
