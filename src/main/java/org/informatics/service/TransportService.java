package org.informatics.service;

import org.informatics.dao.TransportDao;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.exception.DriverQualificationException;
import org.informatics.exception.InvalidVehicleForTransportException;
import org.informatics.exception.MissingRequiredDataException;

/**
 * Service class for managing transport operations.
 * Handles business logic validation before persisting transports to the database.
 */
public class TransportService {
    /**
     * Creates and persists new transport after validating business rules.
     *
     * Business rules:
     * - PassengerTransport must use a Bus
     * - PassengerTransport with > 12 passengers requires PASSENGERS_OVER_12 qualification
     * - CargoTransport must use a Truck or Tanker
     * - CargoTransport with flammable Tanker requires SPECIAL_CARGO qualification
     *
     * @param transport the transport to create
     * @throws MissingRequiredDataException if required fields are null
     * @throws InvalidVehicleForTransportException if wrong vehicle type is used
     * @throws DriverQualificationException if driver lacks required qualification
     */
    public static void createTransport(Transport transport) {
        validateTransport(transport);
        TransportDao.create(transport);
    }

    /**
     * Validates transport against business rules.
     *
     * @param transport the transport to validate
     * @throws MissingRequiredDataException if required fields are null
     * @throws InvalidVehicleForTransportException if wrong vehicle type is used
     * @throws DriverQualificationException if driver lacks required qualification
     */
    private static void validateTransport(Transport transport) {
        // Validate required fields
        if (transport.getCompany() == null ||
                transport.getClient() == null ||
                transport.getDriver() == null ||
                transport.getVehicle() == null) {
            throw new MissingRequiredDataException("Company, Client, Driver and Vehicle are required for Transport.");
        }

        // Validate passenger transport
        if (transport instanceof PassengerTransport pt) {
            // Rule 1: PassengerTransport must use a Bus
            if (!(transport.getVehicle() instanceof Bus)) {
                throw new InvalidVehicleForTransportException("PassengerTransport must use a Bus.");
            }

            // Rule 2: Transporting > 12 passengers requires PASSENGERS_OVER_12 qualification
            if (pt.getPassengerCount() > 12 &&
                    !transport.getDriver().getQualifications().contains(DriverQualification.PASSENGERS_OVER_12)) {
                throw new DriverQualificationException("Driver must have PASSENGERS_OVER_12 qualification for more than 12 passengers.");
            }
        }

        // Validate cargo transport
        if (transport instanceof CargoTransport) {
            // Rule 3: CargoTransport must use a Truck or Tanker
            if (!(transport.getVehicle() instanceof Truck) && !(transport.getVehicle() instanceof Tanker)) {
                throw new InvalidVehicleForTransportException("CargoTransport must use a Truck or a Tanker.");
            }

            // Rule 4: Flammable cargo requires SPECIAL_CARGO qualification
            if (transport.getVehicle() instanceof Tanker tanker && tanker.isFlammable() &&
                    !transport.getDriver().getQualifications().contains(DriverQualification.SPECIAL_CARGO)) {
                throw new DriverQualificationException("Driver must have SPECIAL_CARGO qualification for flammable/special cargo.");
            }
        }
    }
}
