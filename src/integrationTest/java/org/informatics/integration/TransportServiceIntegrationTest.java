package org.informatics.integration;

import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.exception.DriverQualificationException;
import org.informatics.exception.InvalidVehicleForTransportException;
import org.informatics.service.TransportService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransportServiceIntegrationTest {

    @Test
    @Order(1)
    void passengerTransport_over12_withoutQualification_shouldThrow() {
        // Arrange - Create test data
        TransportCompany company = createCompany("TestCo A - " + System.currentTimeMillis());
        Client client = createClient("Ivan", "Petrov", "0888" + System.currentTimeMillis() % 1000000);

        Driver driver = new Driver();
        driver.setFirstName("No");
        driver.setLastName("Qual");
        driver.setSalary(1000);
        driver.setCompany(company);
        EmployeeDao.create(driver);

        Bus bus = new Bus();
        bus.setRegistrationNumber("TEST-BUS-" + System.currentTimeMillis() % 10000);
        bus.setBrand("Brand");
        bus.setModel("Model");
        bus.setSeats(50);
        bus.setCompany(company);
        VehicleDao.create(bus);

        // Arrange - Create passenger transport with > 12 passengers
        PassengerTransport pt = new PassengerTransport();
        pt.setCompany(company);
        pt.setClient(client);
        pt.setDriver(driver);
        pt.setVehicle(bus);
        pt.setDestination("Sofia");
        pt.setTransportDate(LocalDate.now());
        pt.setPrice(100);
        pt.setPaymentStatus(PaymentStatus.UNPAID);
        pt.setPassengerCount(20); // Over 12, but driver has no qualification

        // Act & Assert - Should throw exception
        assertThrows(DriverQualificationException.class,
                () -> TransportService.createTransport(pt),
                "Driver without PASSENGERS_OVER_12 qualification should not transport > 12 passengers");
    }

    @Test
    @Order(2)
    void passengerTransport_withTruck_shouldThrow() {
        // Arrange - Create test data
        TransportCompany company = createCompany("TestCo B - " + System.currentTimeMillis());
        Client client = createClient("Maria", "Ivanova", "0888" + System.currentTimeMillis() % 1000000);

        Driver driver = new Driver();
        driver.setFirstName("Passenger");
        driver.setLastName("Driver");
        driver.setSalary(1200);
        driver.setCompany(company);
        driver.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(driver);

        Truck truck = new Truck();
        truck.setRegistrationNumber("TEST-TRK-" + System.currentTimeMillis() % 10000);
        truck.setBrand("Brand");
        truck.setModel("Model");
        truck.setMaxLoadKg(10000);
        truck.setCompany(company);
        VehicleDao.create(truck);

        // Arrange - Create passenger transport with wrong vehicle type
        PassengerTransport pt = new PassengerTransport();
        pt.setCompany(company);
        pt.setClient(client);
        pt.setDriver(driver);
        pt.setVehicle(truck); // Wrong: should be Bus
        pt.setDestination("Plovdiv");
        pt.setTransportDate(LocalDate.now());
        pt.setPrice(100);
        pt.setPaymentStatus(PaymentStatus.UNPAID);
        pt.setPassengerCount(20);

        // Act & Assert - Should throw exception
        assertThrows(InvalidVehicleForTransportException.class,
                () -> TransportService.createTransport(pt),
                "PassengerTransport must use a Bus, not a Truck");
    }

    @Test
    @Order(3)
    void cargoTransport_flammableTanker_withoutSpecialCargo_shouldThrow() {
        // Arrange - Create test data
        TransportCompany company = createCompany("TestCo C - " + System.currentTimeMillis());
        Client client = createClient("Niki", "Petkov", "0888" + System.currentTimeMillis() % 1000000);

        Driver driver = new Driver();
        driver.setFirstName("Cargo");
        driver.setLastName("Driver");
        driver.setSalary(1500);
        driver.setCompany(company);
        // No SPECIAL_CARGO qualification
        EmployeeDao.create(driver);

        Tanker tanker = new Tanker();
        tanker.setRegistrationNumber("TEST-TNK-" + System.currentTimeMillis() % 10000);
        tanker.setBrand("Brand");
        tanker.setModel("Model");
        tanker.setMaxLiters(10000);
        tanker.setFlammable(true); // Flammable cargo
        tanker.setCompany(company);
        VehicleDao.create(tanker);

        // Arrange - Create cargo transport with flammable tanker
        CargoTransport ct = new CargoTransport();
        ct.setCompany(company);
        ct.setClient(client);
        ct.setDriver(driver);
        ct.setVehicle(tanker);
        ct.setDestination("Varna");
        ct.setTransportDate(LocalDate.now());
        ct.setPrice(500);
        ct.setPaymentStatus(PaymentStatus.UNPAID);
        ct.setCargoWeightKg(1000);

        // Act & Assert - Should throw exception
        assertThrows(DriverQualificationException.class,
                () -> TransportService.createTransport(ct),
                "Driver without SPECIAL_CARGO qualification should not transport flammable cargo");
    }

    @Test
    @Order(4)
    void cargoTransport_truck_shouldPass() {
        // Arrange - Create test data
        TransportCompany company = createCompany("TestCo D - " + System.currentTimeMillis());
        Client client = createClient("Stefan", "Iliev", "0888" + System.currentTimeMillis() % 1000000);

        Driver driver = new Driver();
        driver.setFirstName("Truck");
        driver.setLastName("Driver");
        driver.setSalary(1600);
        driver.setCompany(company);
        EmployeeDao.create(driver);

        Truck truck = new Truck();
        truck.setRegistrationNumber("TEST-TRK-" + System.currentTimeMillis() % 10000);
        truck.setBrand("Brand");
        truck.setModel("Model");
        truck.setMaxLoadKg(10000);
        truck.setCompany(company);
        VehicleDao.create(truck);

        // Arrange - Create valid cargo transport
        CargoTransport ct = new CargoTransport();
        ct.setCompany(company);
        ct.setClient(client);
        ct.setDriver(driver);
        ct.setVehicle(truck);
        ct.setDestination("Burgas");
        ct.setTransportDate(LocalDate.now());
        ct.setPrice(700);
        ct.setPaymentStatus(PaymentStatus.PAID);
        ct.setCargoWeightKg(2000);

        // Act & Assert - Should NOT throw exception
        assertDoesNotThrow(() -> TransportService.createTransport(ct),
                "Valid cargo transport with truck should be created successfully");
    }

    @Test
    @Order(5)
    void passengerTransport_over12_withQualification_shouldPass() {
        // Arrange - Create test data
        TransportCompany company = createCompany("HappyCo A - " + System.currentTimeMillis());
        Client client = createClient("Ana", "Nikolova", "0888" + System.currentTimeMillis() % 1000000);

        Driver driver = new Driver();
        driver.setFirstName("Qualified");
        driver.setLastName("Passenger");
        driver.setSalary(1500);
        driver.setCompany(company);
        driver.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(driver);

        Bus bus = new Bus();
        bus.setRegistrationNumber("HP-BUS-" + System.currentTimeMillis() % 10000);
        bus.setBrand("Setra");
        bus.setModel("Demo");
        bus.setSeats(50);
        bus.setCompany(company);
        VehicleDao.create(bus);

        // Arrange - Create valid passenger transport
        PassengerTransport pt = new PassengerTransport();
        pt.setCompany(company);
        pt.setClient(client);
        pt.setDriver(driver);
        pt.setVehicle(bus);
        pt.setDestination("Sofia");
        pt.setTransportDate(LocalDate.now());
        pt.setPrice(200);
        pt.setPaymentStatus(PaymentStatus.UNPAID);
        pt.setPassengerCount(20); // Over 12, but driver HAS qualification

        // Act & Assert - Should NOT throw exception
        assertDoesNotThrow(() -> TransportService.createTransport(pt),
                "Qualified driver should be able to transport > 12 passengers");
    }

    @Test
    @Order(6)
    void cargoTransport_flammableTanker_withSpecialCargo_shouldPass() {
        // Arrange - Create test data
        TransportCompany company = createCompany("HappyCo B - " + System.currentTimeMillis());
        Client client = createClient("Viktor", "Marinov", "0888" + System.currentTimeMillis() % 1000000);

        Driver driver = new Driver();
        driver.setFirstName("Qualified");
        driver.setLastName("Cargo");
        driver.setSalary(1700);
        driver.setCompany(company);
        driver.getQualifications().add(DriverQualification.SPECIAL_CARGO);
        EmployeeDao.create(driver);

        Tanker tanker = new Tanker();
        tanker.setRegistrationNumber("HP-TNK-" + System.currentTimeMillis() % 10000);
        tanker.setBrand("MAN");
        tanker.setModel("Demo");
        tanker.setMaxLiters(10000);
        tanker.setFlammable(true);
        tanker.setCompany(company);
        VehicleDao.create(tanker);

        // Arrange - Create valid flammable cargo transport
        CargoTransport ct = new CargoTransport();
        ct.setCompany(company);
        ct.setClient(client);
        ct.setDriver(driver);
        ct.setVehicle(tanker);
        ct.setDestination("Varna");
        ct.setTransportDate(LocalDate.now());
        ct.setPrice(500);
        ct.setPaymentStatus(PaymentStatus.PAID);
        ct.setCargoWeightKg(3000);

        // Act & Assert - Should NOT throw exception
        assertDoesNotThrow(() -> TransportService.createTransport(ct),
                "Qualified driver should be able to transport flammable cargo");
    }

    // ========== HELPER METHODS ==========

    /**
     * Creates and persists a TransportCompany with the given name.
     * Returns the persisted company entity.
     */
    private static TransportCompany createCompany(String name) {
        TransportCompany c = new TransportCompany();
        c.setName(name);
        TransportCompanyDao.create(c);

        return TransportCompanyDao.getAll().stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to create company: " + name));
    }

    /**
     * Creates and persists a Client with the given details.
     * Returns the persisted client entity.
     */
    private static Client createClient(String firstName, String lastName, String phone) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setPhone(phone);
        ClientDao.create(client);

        return ClientDao.getAll().stream()
                .filter(x -> phone.equals(x.getPhone()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to create client: " + phone));
    }
}