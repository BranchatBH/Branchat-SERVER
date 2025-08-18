package com.b.h.Branchat.domain.auth.exception;

import com.b.h.Branchat.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {
    GOOGLE_TOKEN_RETRIEVAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "구글 토큰 발급에 실패했습니다.");


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
