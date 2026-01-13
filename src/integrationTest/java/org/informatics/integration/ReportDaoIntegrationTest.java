package org.informatics.integration;

import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.service.TransportService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReportDaoIntegrationTest {

    private static long companyId;
    private static long clientId;
    private static long driverId;
    private static long busId;

    @BeforeAll
    static void setup() {
        // Create test data once
        TransportCompany company = new TransportCompany();
        company.setName("RevenueCo - " + System.currentTimeMillis());
        TransportCompanyDao.create(company);
        companyId = TransportCompanyDao.getAll().stream()
                .filter(c -> c.getName().startsWith("RevenueCo"))
                .findFirst().orElseThrow().getId();

        Client client = new Client();
        client.setFirstName("Ivan");
        client.setLastName("Petrov");
        client.setPhone("0888" + System.currentTimeMillis() % 1000000);
        ClientDao.create(client);
        clientId = ClientDao.getAll().stream()
                .filter(c -> c.getPhone().equals(client.getPhone()))
                .findFirst().orElseThrow().getId();

        Driver driver = new Driver();
        driver.setFirstName("Elena");
        driver.setLastName("Driver");
        driver.setSalary(2000);
        driver.setCompany(TransportCompanyDao.get(companyId));
        driver.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(driver);
        driverId = DriverDao.getAllDrivers().stream()
                .filter(d -> d.getFirstName().equals("Elena") && d.getLastName().equals("Driver"))
                .findFirst().orElseThrow().getId();

        Bus bus = new Bus();
        bus.setRegistrationNumber("REV-BUS-" + System.currentTimeMillis() % 10000);
        bus.setBrand("Mercedes");
        bus.setModel("Demo");
        bus.setSeats(49);
        bus.setCompany(TransportCompanyDao.get(companyId));
        VehicleDao.create(bus);
        busId = VehicleDao.getAll().stream()
                .filter(v -> v.getRegistrationNumber().startsWith("REV-BUS"))
                .findFirst().orElseThrow().getId();
    }

    @Test
    @Order(1)
    void revenue_shouldCountOnlyPaid() {
        TransportCompany company = TransportCompanyDao.get(companyId);
        Client client = ClientDao.get(clientId);
        Driver driver = (Driver) EmployeeDao.getAll().stream()
                .filter(e -> e.getId() == driverId)
                .findFirst().orElseThrow();
        Bus bus = (Bus) VehicleDao.get(busId);

        // Get revenue BEFORE adding new transports
        double revenueBefore = ReportDao.getTotalTransportsRevenue();

        // PAID transport
        PassengerTransport paid = new PassengerTransport();
        paid.setCompany(company);
        paid.setClient(client);
        paid.setDriver(driver);
        paid.setVehicle(bus);
        paid.setDestination("Sofia");
        paid.setTransportDate(LocalDate.now());
        paid.setPrice(100);
        paid.setPaymentStatus(PaymentStatus.PAID);
        paid.setPassengerCount(20);
        TransportService.createTransport(paid);

        // UNPAID transport
        PassengerTransport unpaid = new PassengerTransport();
        unpaid.setCompany(company);
        unpaid.setClient(client);
        unpaid.setDriver(driver);
        unpaid.setVehicle(bus);
        unpaid.setDestination("Plovdiv");
        unpaid.setTransportDate(LocalDate.now());
        unpaid.setPrice(999);
        unpaid.setPaymentStatus(PaymentStatus.UNPAID);
        unpaid.setPassengerCount(20);
        TransportService.createTransport(unpaid);

        // Get revenue AFTER
        double revenueAfter = ReportDao.getTotalTransportsRevenue();

        // Revenue should INCREASE by 100 (only PAID transport)
        assertEquals(revenueBefore + 100.0, revenueAfter, 0.0001,
                "Revenue should increase by 100 (only PAID transport counted)");

        // Total value should increase by 1099 (PAID + UNPAID)
        double totalValueAfter = ReportDao.getTotalTransportsValue();
        assertTrue(totalValueAfter >= revenueBefore + 1099.0,
                "Total value should include both PAID and UNPAID transports");
    }

    @Test
    @Order(2)
    void driverReports_shouldWork() {
        var driversWithCount = ReportDao.getDriversWithTransportsCount();
        assertFalse(driversWithCount.isEmpty(), "Should have drivers with transport count");

        var revenueByDriver = ReportDao.getRevenueByDriver();
        assertFalse(revenueByDriver.isEmpty(), "Should have driver revenue data");
    }

    @Test
    @Order(3)
    void periodRevenue_shouldWork() {
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        double revenue = ReportDao.getRevenueForPeriod(from, to);
        assertTrue(revenue >= 0, "Revenue for period should be non-negative");
    }
}