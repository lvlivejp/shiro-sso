package com.lvlivejp.shirosso.exception;

public class BusinessException extends RuntimeException {
    /**
     * 错误代码
     **/
    public String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
