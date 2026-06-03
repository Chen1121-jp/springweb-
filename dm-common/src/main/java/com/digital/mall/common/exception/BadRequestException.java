package com.digital.mall.common.exception;

public class BadRequestException extends CommonException {
    public BadRequestException(String message) {
        super(400, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(400, message, cause);
    }
}
