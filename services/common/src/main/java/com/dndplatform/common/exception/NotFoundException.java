package com.dndplatform.common.exception;

/**
 * Exception for 404 Not Found responses.
 * Use when the requested resource does not exist.
 */
public class NotFoundException extends ApplicationException {

    private static final int STATUS_CODE = 404;

    public NotFoundException(String message) {
        super(message, STATUS_CODE);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, STATUS_CODE, cause);
    }
}