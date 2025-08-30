package com.b.h.Branchat.domain.node.exception;

import com.b.h.Branchat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NodeErrorCode implements ErrorCode {
    //403
    FORBIDDEN_NODE_ACCESS(HttpStatus.FORBIDDEN, "You do not have permission to access this node"),

    //404
    PARENT_NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "Parent node not found");

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