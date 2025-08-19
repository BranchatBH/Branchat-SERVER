package com.b.h.Branchat.domain.auth.exception;

import com.b.h.Branchat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    //401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "너 뉘기야."),

    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),

    //500
    GOOGLE_USER_INFO_RETRIEVAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "구글 사용자 정보 조회에 실패했습니다."),
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
