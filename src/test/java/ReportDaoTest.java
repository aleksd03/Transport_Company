import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.service.TransportService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportDaoTest {

    @Test
    void revenue_shouldCountOnlyPaid() {
        TransportCompany company = new TransportCompany();
        company.setName("RevenueCo");
        TransportCompanyDao.create(company);

        Client client = new Client();
        client.setFirstName("Ivan");
        client.setLastName("Petrov");
        client.setPhone("0888000201");
        ClientDao.create(client);

        Driver driver = new Driver();
        driver.setFirstName("Elena");
        driver.setLastName("Driver");
        driver.setSalary(2000);
        driver.setCompany(company);
        driver.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(driver);

        Bus bus = new Bus();
        bus.setRegistrationNumber("REV-BUS-001");
        bus.setBrand("Mercedes");
        bus.setModel("Demo");
        bus.setSeats(49);
        bus.setCompany(company);
        VehicleDao.create(bus);

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

        assertEquals(100.0, ReportDao.getTotalTransportsRevenue(), 0.0001);
        assertEquals(1099.0, ReportDao.getTotalTransportsValue(), 0.0001);
    }
}
