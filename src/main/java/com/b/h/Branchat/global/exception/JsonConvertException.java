package com.b.h.Branchat.global.exception;

public class JsonConvertException extends BusinessException {

    public JsonConvertException(ErrorCode errorCode) {
        super(errorCode);
    }

    public JsonConvertException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
