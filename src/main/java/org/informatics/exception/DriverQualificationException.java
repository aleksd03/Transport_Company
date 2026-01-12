package org.informatics.exception;

/**
 * Thrown when a driver lacks the required qualification to perform transport.
 */
public class DriverQualificationException extends AppException {
    public DriverQualificationException(String message) {
        super(message);
    }
}
