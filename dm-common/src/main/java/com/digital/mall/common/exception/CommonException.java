package com.digital.mall.common.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final int code;

    public CommonException(String message) {
        this(500, message);
    }

    public CommonException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
