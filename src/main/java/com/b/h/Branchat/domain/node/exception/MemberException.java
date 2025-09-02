package com.b.h.Branchat.domain.node.exception;

import com.b.h.Branchat.global.exception.BusinessException;
import com.b.h.Branchat.global.exception.ErrorCode;

public class MemberException extends BusinessException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
