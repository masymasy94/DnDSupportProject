package com.dndplatform.common.exception;

public class BadGatewayException extends ApplicationException {

    private static final int STATUS_CODE = 502;

    public BadGatewayException(String message) {
        super(message, STATUS_CODE);
    }

    public BadGatewayException(String message, Throwable cause) {
        super(message, STATUS_CODE, cause);
    }
}