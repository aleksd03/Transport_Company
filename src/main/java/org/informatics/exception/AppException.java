package org.informatics.exception;

/**
 * Base exception class for all application-specific exceptions.
 */
public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}