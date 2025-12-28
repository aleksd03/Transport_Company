package org.informatics.integration;

import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.service.TransportService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DaoIntegrationTest {

    @Test
    void transportCreation_shouldPersist_andReportsShouldWork() {
        // Company
        TransportCompany company = new TransportCompany();
        company.setName("IT Company");
        TransportCompanyDao.create(company);

        // Client
        Client client = new Client();
        client.setFirstName("Ivan");
        client.setLastName("Petrov");
        client.setPhone("0899000000");
        ClientDao.create(client);

        // Driver
        Driver driver = new Driver();
        driver.setFirstName("Elena");
        driver.setLastName("Ivanova");
        driver.setSalary(2500);
        driver.setCompany(company);
        EmployeeDao.create(driver);

        // Vehicle
        Truck truck = new Truck();
        truck.setRegistrationNumber("IT-TRK-001");
        truck.setBrand("Volvo");
        truck.setModel("FH");
        truck.setMaxLoadKg(15000);
        truck.setCompany(company);
        VehicleDao.create(truck);

        // Transport (PAID)
        CargoTransport ct = new CargoTransport();
        ct.setCompany(company);
        ct.setClient(client);
        ct.setDriver(driver);
        ct.setVehicle(truck);
        ct.setDestination("Sofia");
        ct.setTransportDate(LocalDate.now());
        ct.setPrice(1000);
        ct.setPaymentStatus(PaymentStatus.PAID);
        ct.setCargoWeightKg(5000);

        TransportService.createTransport(ct);

        // Assert persisted
        assertEquals(1, TransportDao.getAll().size());

        // Revenue should count PAID only
        assertEquals(1000.0, ReportDao.getTotalTransportsRevenue(), 0.0001);

        // Company revenue sorting should include this company
        var companiesByRevenue = TransportCompanyDao.getAllSortedByRevenueDesc();
        assertFalse(companiesByRevenue.isEmpty());
        assertEquals("IT Company", companiesByRevenue.get(0).getCompanyName());
    }
}
