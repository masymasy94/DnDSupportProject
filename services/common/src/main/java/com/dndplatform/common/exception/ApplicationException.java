package com.dndplatform.common.exception;

/**
 * Base exception for all application-level exceptions.
 * Extend this class to create specific exceptions with HTTP status codes.
 */
public abstract class ApplicationException extends RuntimeException {

    private final int statusCode;

    protected ApplicationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    protected ApplicationException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}