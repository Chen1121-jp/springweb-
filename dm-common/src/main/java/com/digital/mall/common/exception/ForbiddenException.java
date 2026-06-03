package com.digital.mall.common.exception;

public class ForbiddenException extends CommonException {
    public ForbiddenException(String message) {
        super(403, message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(403, message, cause);
    }
}
