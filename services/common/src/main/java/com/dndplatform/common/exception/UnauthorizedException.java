package com.dndplatform.common.exception;

/**
 * Exception for 401 Unauthorized responses.
 * Use when authentication is required but not provided or invalid.
 */
public class UnauthorizedException extends ApplicationException {

    private static final int STATUS_CODE = 401;

    public UnauthorizedException(String message) {
        super(message, STATUS_CODE);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, STATUS_CODE, cause);
    }
}