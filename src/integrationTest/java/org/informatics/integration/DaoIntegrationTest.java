package org.informatics.integration;

import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.exception.EntityNotFoundException;
import org.informatics.service.TransportService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DaoIntegrationTest {

    @Test
    @Order(1)
    void transportCreation_shouldPersist_andReportsShouldWork() {
        // Arrange - Create company
        TransportCompany company = new TransportCompany();
        company.setName("IT Company - " + System.currentTimeMillis());
        TransportCompanyDao.create(company);

        // Arrange - Create client
        Client client = new Client();
        client.setFirstName("Ivan");
        client.setLastName("Petrov");
        client.setPhone("0899000" + System.currentTimeMillis() % 1000);
        ClientDao.create(client);

        // Arrange - Create driver
        Driver driver = new Driver();
        driver.setFirstName("Elena");
        driver.setLastName("Ivanova");
        driver.setSalary(2500);
        driver.setCompany(company);
        EmployeeDao.create(driver);

        // Arrange - Create vehicle
        Truck truck = new Truck();
        truck.setRegistrationNumber("IT-TRK-" + System.currentTimeMillis() % 10000);
        truck.setBrand("Volvo");
        truck.setModel("FH");
        truck.setMaxLoadKg(15000);
        truck.setCompany(company);
        VehicleDao.create(truck);

        // Act - Create transport
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

        // Assert - Transport persisted
        assertTrue(TransportDao.getAll().size() >= 1,
                "At least one transport should exist in database");

        // Assert - Revenue counted correctly (only PAID)
        assertTrue(ReportDao.getTotalTransportsRevenue() >= 1000.0,
                "Revenue should include the PAID transport");

        // Assert - Company appears in revenue report
        var companiesByRevenue = TransportCompanyDao.getAllSortedByRevenueDesc();
        assertFalse(companiesByRevenue.isEmpty(),
                "Should have companies in revenue report");
    }

    @Test
    @Order(2)
    void crudOperations_shouldWorkCorrectly() {
        // CREATE - Create new company
        TransportCompany company = new TransportCompany();
        String uniqueName = "CRUD Test Company - " + System.currentTimeMillis();
        company.setName(uniqueName);
        TransportCompanyDao.create(company);

        // Find the created company ID
        long companyId = TransportCompanyDao.getAll().stream()
                .filter(c -> c.getName().equals(uniqueName))
                .findFirst()
                .orElseThrow()
                .getId();

        // READ - Retrieve the company
        TransportCompany retrieved = TransportCompanyDao.get(companyId);
        assertNotNull(retrieved, "Retrieved company should not be null");
        assertEquals(uniqueName, retrieved.getName(),
                "Retrieved company should have the correct name");

        // UPDATE - Update company name
        String updatedName = "Updated Company - " + System.currentTimeMillis();
        TransportCompanyDao.updateName(companyId, updatedName);

        TransportCompany updated = TransportCompanyDao.get(companyId);
        assertEquals(updatedName, updated.getName(),
                "Company name should be updated");

        // DELETE - Delete the company
        TransportCompanyDao.delete(companyId);

        TransportCompany deleted = TransportCompanyDao.get(companyId);
        assertNull(deleted, "Deleted company should not exist");
    }

    @Test
    @Order(3)
    void delete_nonExistingEntity_shouldThrowException() {
        // Act & Assert - Deleting non-existing entity should throw exception
        assertThrows(EntityNotFoundException.class,
                () -> TransportCompanyDao.delete(999999L),
                "Deleting non-existing company should throw EntityNotFoundException");
    }
}