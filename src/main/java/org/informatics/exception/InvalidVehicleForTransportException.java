package org.informatics.exception;

/**
 * Thrown when an incorrect vehicle type is used for transport.
 * Example: attempting to use a Truck for PassengerTransport.
 */
public class InvalidVehicleForTransportException extends AppException {
    public InvalidVehicleForTransportException(String message) {
        super(message);
    }
}
