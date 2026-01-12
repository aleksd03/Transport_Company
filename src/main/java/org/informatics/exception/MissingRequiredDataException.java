package org.informatics.exception;

/**
 * Thrown when required fields are missing when creating transport.
 */
public class MissingRequiredDataException extends AppException {
    public MissingRequiredDataException(String message) {
        super(message);
    }
}
