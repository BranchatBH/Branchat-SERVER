package com.b.h.Branchat.domain.auth.exception;

import com.b.h.Branchat.global.exception.BusinessException;

public class AuthException extends BusinessException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }
    public AuthException(AuthErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
