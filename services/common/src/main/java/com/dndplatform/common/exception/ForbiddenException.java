package com.dndplatform.common.exception;

/**
 * Exception for 403 Forbidden responses.
 * Use when the user is authenticated but not authorized to access the resource.
 */
public class ForbiddenException extends ApplicationException {

    private static final int STATUS_CODE = 403;

    public ForbiddenException(String message) {
        super(message, STATUS_CODE);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, STATUS_CODE, cause);
    }
}