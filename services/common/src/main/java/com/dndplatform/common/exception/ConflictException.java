package com.dndplatform.common.exception;

/**
 * Exception for 409 Conflict responses.
 * Use when the request conflicts with the current state (e.g., duplicate resource).
 */
public class ConflictException extends ApplicationException {

    private static final int STATUS_CODE = 409;

    public ConflictException(String message) {
        super(message, STATUS_CODE);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, STATUS_CODE, cause);
    }
}