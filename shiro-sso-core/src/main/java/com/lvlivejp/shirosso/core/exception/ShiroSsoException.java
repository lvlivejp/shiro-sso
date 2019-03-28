package com.lvlivejp.shirosso.core.exception;

public class ShiroSsoException extends RuntimeException {
    /**
     * 错误代码
     **/
    public String errorCode;

    public ShiroSsoException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
