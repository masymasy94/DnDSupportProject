package com.dndplatform.common.exception;

/**
 * Exception for 400 Bad Request responses related to invalid template syntax.
 * Use when template validation fails (e.g., Qute compilation error).
 */
public class InvalidTemplateException extends ApplicationException {

    private static final int STATUS_CODE = 400;

    public InvalidTemplateException(String message) {
        super(message, STATUS_CODE);
    }

    public InvalidTemplateException(String message, Throwable cause) {
        super(message, STATUS_CODE, cause);
    }
}
