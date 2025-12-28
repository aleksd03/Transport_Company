package org.informatics.service;

import org.informatics.dao.TransportDao;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.exception.DriverQualificationException;
import org.informatics.exception.InvalidVehicleForTransportException;
import org.informatics.exception.MissingRequiredDataException;


public class TransportService {
    public static void createTransport(Transport transport) {
        validateTransport(transport);
        TransportDao.create(transport);
    }

    private static void validateTransport(Transport transport) {
        if (transport.getCompany() == null ||
                transport.getClient() == null ||
                transport.getDriver() == null ||
                transport.getVehicle() == null) {
            throw new MissingRequiredDataException("Company, Client, Driver and Vehicle are required for Transport.");
        }

        if (transport instanceof PassengerTransport pt) {
            if (!(transport.getVehicle() instanceof Bus)) {
                throw new InvalidVehicleForTransportException("PassengerTransport must use a Bus.");
            }
            if (pt.getPassengerCount() > 12 &&
                    !transport.getDriver().getQualifications().contains(DriverQualification.PASSENGERS_OVER_12)) {
                throw new DriverQualificationException("Driver must have PASSENGERS_OVER_12 qualification for more than 12 passengers.");
            }
        }

        // 2) Товари: камион/танкер; ако е леснозапалимо (tanker flammable) -> SPECIAL_CARGO
        if (transport instanceof CargoTransport) {
            if (!(transport.getVehicle() instanceof Truck) && !(transport.getVehicle() instanceof Tanker)) {
                throw new InvalidVehicleForTransportException("CargoTransport must use a Truck or a Tanker.");
            }
            if (transport.getVehicle() instanceof Tanker tanker && tanker.isFlammable() &&
                    !transport.getDriver().getQualifications().contains(DriverQualification.SPECIAL_CARGO)) {
                throw new DriverQualificationException("Driver must have SPECIAL_CARGO qualification for flammable/special cargo.");
            }
        }
    }
}
