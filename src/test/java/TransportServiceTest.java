import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.exception.DriverQualificationException;
import org.informatics.exception.InvalidVehicleForTransportException;
import org.informatics.service.TransportService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TransportServiceTest {
    @Test
    void passengerTransport_over12_withoutQualification_shouldThrow() {
        TransportCompany company = createCompany("TestCo A");

        Client client = createClient("Ivan", "Petrov", "0888000001");

        Driver driver = new Driver();
        driver.setFirstName("No");
        driver.setLastName("Qual");
        driver.setSalary(1000);
        driver.setCompany(company);
        EmployeeDao.create(driver);

        Bus bus = new Bus();
        bus.setRegistrationNumber("TEST-BUS-001");
        bus.setBrand("Brand");
        bus.setModel("Model");
        bus.setSeats(50);
        bus.setCompany(company);
        VehicleDao.create(bus);

        PassengerTransport pt = new PassengerTransport();
        pt.setCompany(company);
        pt.setClient(client);
        pt.setDriver(driver);
        pt.setVehicle(bus);
        pt.setDestination("Sofia");
        pt.setTransportDate(LocalDate.now());
        pt.setPrice(100);
        pt.setPaymentStatus(PaymentStatus.UNPAID);
        pt.setPassengerCount(20);

        assertThrows(DriverQualificationException.class, () -> TransportService.createTransport(pt));
    }

    @Test
    void passengerTransport_withTruck_shouldThrow() {
        TransportCompany company = createCompany("TestCo B");
        Client client = createClient("Maria", "Ivanova", "0888000002");

        Driver driver = new Driver();
        driver.setFirstName("Passenger");
        driver.setLastName("Driver");
        driver.setSalary(1200);
        driver.setCompany(company);
        driver.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(driver);

        Truck truck = new Truck();
        truck.setRegistrationNumber("TEST-TRK-001");
        truck.setBrand("Brand");
        truck.setModel("Model");
        truck.setMaxLoadKg(10000);
        truck.setCompany(company);
        VehicleDao.create(truck);

        PassengerTransport pt = new PassengerTransport();
        pt.setCompany(company);
        pt.setClient(client);
        pt.setDriver(driver);
        pt.setVehicle(truck); // invalid
        pt.setDestination("Plovdiv");
        pt.setTransportDate(LocalDate.now());
        pt.setPrice(100);
        pt.setPaymentStatus(PaymentStatus.UNPAID);
        pt.setPassengerCount(20);

        assertThrows(InvalidVehicleForTransportException.class, () -> TransportService.createTransport(pt));
    }

    @Test
    void cargoTransport_flammableTanker_withoutSpecialCargo_shouldThrow() {
        TransportCompany company = createCompany("TestCo C");
        Client client = createClient("Niki", "Petkov", "0888000003");

        Driver driver = new Driver();
        driver.setFirstName("Cargo");
        driver.setLastName("Driver");
        driver.setSalary(1500);
        driver.setCompany(company);
        // no SPECIAL_CARGO
        EmployeeDao.create(driver);

        Tanker tanker = new Tanker();
        tanker.setRegistrationNumber("TEST-TNK-001");
        tanker.setBrand("Brand");
        tanker.setModel("Model");
        tanker.setMaxLiters(10000);
        tanker.setFlammable(true);
        tanker.setCompany(company);
        VehicleDao.create(tanker);

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

        assertThrows(DriverQualificationException.class, () -> TransportService.createTransport(ct));
    }

    @Test
    void cargoTransport_truck_shouldPass() {
        TransportCompany company = createCompany("TestCo D");
        Client client = createClient("Stefan", "Iliev", "0888000004");

        Driver driver = new Driver();
        driver.setFirstName("Truck");
        driver.setLastName("Driver");
        driver.setSalary(1600);
        driver.setCompany(company);
        EmployeeDao.create(driver);

        Truck truck = new Truck();
        truck.setRegistrationNumber("TEST-TRK-002");
        truck.setBrand("Brand");
        truck.setModel("Model");
        truck.setMaxLoadKg(10000);
        truck.setCompany(company);
        VehicleDao.create(truck);

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

        assertDoesNotThrow(() -> TransportService.createTransport(ct));
    }

    @Test
    void passengerTransport_over12_withQualification_shouldPass() {
        TransportCompany company = createCompany("HappyCo A");
        Client client = createClient("Ana", "Nikolova", "0888000101");

        Driver driver = new Driver();
        driver.setFirstName("Qualified");
        driver.setLastName("Passenger");
        driver.setSalary(1500);
        driver.setCompany(company);
        driver.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(driver);

        Bus bus = new Bus();
        bus.setRegistrationNumber("HP-BUS-100");
        bus.setBrand("Setra");
        bus.setModel("Demo");
        bus.setSeats(50);
        bus.setCompany(company);
        VehicleDao.create(bus);

        PassengerTransport pt = new PassengerTransport();
        pt.setCompany(company);
        pt.setClient(client);
        pt.setDriver(driver);
        pt.setVehicle(bus);
        pt.setDestination("Sofia");
        pt.setTransportDate(LocalDate.now());
        pt.setPrice(200);
        pt.setPaymentStatus(PaymentStatus.UNPAID);
        pt.setPassengerCount(20);

        assertDoesNotThrow(() -> TransportService.createTransport(pt));
    }

    @Test
    void cargoTransport_flammableTanker_withSpecialCargo_shouldPass() {
        TransportCompany company = createCompany("HappyCo B");
        Client client = createClient("Viktor", "Marinov", "0888000102");

        Driver driver = new Driver();
        driver.setFirstName("Qualified");
        driver.setLastName("Cargo");
        driver.setSalary(1700);
        driver.setCompany(company);
        driver.getQualifications().add(DriverQualification.SPECIAL_CARGO);
        EmployeeDao.create(driver);

        Tanker tanker = new Tanker();
        tanker.setRegistrationNumber("HP-TNK-100");
        tanker.setBrand("MAN");
        tanker.setModel("Demo");
        tanker.setMaxLiters(10000);
        tanker.setFlammable(true);
        tanker.setCompany(company);
        VehicleDao.create(tanker);

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

        assertDoesNotThrow(() -> TransportService.createTransport(ct));
    }

    // ---------- helpers ----------
    private static TransportCompany createCompany(String name) {
        TransportCompany c = new TransportCompany();
        c.setName(name);
        TransportCompanyDao.create(c);
        return TransportCompanyDao.getAll().stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    private static Client createClient(String firstName, String lastName, String phone) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setPhone(phone);
        ClientDao.create(client);
        return ClientDao.getAll().stream()
                .filter(x -> phone.equals(x.getPhone()))
                .findFirst()
                .orElseThrow();
    }
}