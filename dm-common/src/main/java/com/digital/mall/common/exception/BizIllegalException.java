package com.digital.mall.common.exception;

public class BizIllegalException extends CommonException {
    public BizIllegalException(String message) {
        super(500, message);
    }

    public BizIllegalException(String message, Throwable cause) {
        super(500, message, cause);
    }
}
