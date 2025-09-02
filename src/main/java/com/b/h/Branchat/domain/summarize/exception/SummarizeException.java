package com.b.h.Branchat.domain.summarize.exception;

import com.b.h.Branchat.global.exception.BusinessException;
import com.b.h.Branchat.global.exception.ErrorCode;

public class SummarizeException extends BusinessException {
    public SummarizeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
