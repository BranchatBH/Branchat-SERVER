package com.b.h.Branchat.domain.node.exception;

import com.b.h.Branchat.global.exception.BusinessException;
import com.b.h.Branchat.global.exception.ErrorCode;

public class NodeException extends BusinessException {

    public NodeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
