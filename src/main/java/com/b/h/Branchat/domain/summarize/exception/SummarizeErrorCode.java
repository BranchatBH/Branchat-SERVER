package com.b.h.Branchat.domain.summarize.exception;

import com.b.h.Branchat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SummarizeErrorCode implements ErrorCode {
    //400
    MESSAGE_EMPTY(HttpStatus.BAD_REQUEST, "there is no message");

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
