package com.sm.project.apiPayload.exception.handler;

import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {

    public MemberHandler(ErrorStatus errorCode) {
        super(errorCode);
    }
}
