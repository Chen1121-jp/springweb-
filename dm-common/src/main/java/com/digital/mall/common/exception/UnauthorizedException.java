package com.digital.mall.common.exception;

public class UnauthorizedException extends CommonException {
    public UnauthorizedException(String message) {
        super(401, message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(401, message, cause);
    }
}
