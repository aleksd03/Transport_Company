package org.informatics;

import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.service.TransportJsonService;
import org.informatics.service.TransportService;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        seedDemoData();

        runSortingDemo();
        runReportsDemo();
        runJsonExportDemo();
    }

    private static void seedDemoData() {
        System.out.println("=== SEED DEMO DATA (3 Companies) ===");

        // ===================== Company A =====================
        TransportCompany companyA = new TransportCompany();
        companyA.setName("Alvas Logistics");
        TransportCompanyDao.create(companyA);

        Client aClient1 = new Client();
        aClient1.setFirstName("Maria");
        aClient1.setLastName("Georgieva");
        aClient1.setPhone("0899001122");
        ClientDao.create(aClient1);

        Client aClient2 = new Client();
        aClient2.setFirstName("Hristo");
        aClient2.setLastName("Kostov");
        aClient2.setPhone("0877554433");
        ClientDao.create(aClient2);

        Driver aDriverAll = new Driver();
        aDriverAll.setFirstName("Daniel");
        aDriverAll.setLastName("Kolev");
        aDriverAll.setSalary(3200);
        aDriverAll.setCompany(companyA);
        aDriverAll.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        aDriverAll.getQualifications().add(DriverQualification.SPECIAL_CARGO);
        EmployeeDao.create(aDriverAll);

        Driver aDriverPassengers = new Driver();
        aDriverPassengers.setFirstName("Elena");
        aDriverPassengers.setLastName("Stoyanova");
        aDriverPassengers.setSalary(2800);
        aDriverPassengers.setCompany(companyA);
        aDriverPassengers.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(aDriverPassengers);

        Bus aBus = new Bus();
        aBus.setRegistrationNumber("CA7777TT");
        aBus.setBrand("Setra");
        aBus.setModel("S 516 HD");
        aBus.setSeats(55);
        aBus.setCompany(companyA);
        VehicleDao.create(aBus);

        Tanker aTanker = new Tanker();
        aTanker.setRegistrationNumber("PB9090PP");
        aTanker.setBrand("Scania");
        aTanker.setModel("R 500");
        aTanker.setMaxLiters(24000);
        aTanker.setFlammable(true);
        aTanker.setCompany(companyA);
        VehicleDao.create(aTanker);

        // A1) PassengerTransport (PAID)
        PassengerTransport aPtPaid = new PassengerTransport();
        aPtPaid.setCompany(companyA);
        aPtPaid.setClient(aClient1);
        aPtPaid.setDriver(aDriverPassengers);
        aPtPaid.setVehicle(aBus);
        aPtPaid.setDestination("Sofia");
        aPtPaid.setTransportDate(LocalDate.now().minusDays(3));
        aPtPaid.setPrice(600);
        aPtPaid.setPaymentStatus(PaymentStatus.PAID);
        aPtPaid.setPassengerCount(40);
        TransportService.createTransport(aPtPaid);

        // A2) CargoTransport tanker flammable (UNPAID)
        CargoTransport aCtUnpaid = new CargoTransport();
        aCtUnpaid.setCompany(companyA);
        aCtUnpaid.setClient(aClient2);
        aCtUnpaid.setDriver(aDriverAll);
        aCtUnpaid.setVehicle(aTanker);
        aCtUnpaid.setDestination("Varna");
        aCtUnpaid.setTransportDate(LocalDate.now().minusDays(1));
        aCtUnpaid.setPrice(2500);
        aCtUnpaid.setPaymentStatus(PaymentStatus.UNPAID);
        aCtUnpaid.setCargoWeightKg(12000);
        TransportService.createTransport(aCtUnpaid);

        // ===================== Company B =====================
        TransportCompany companyB = new TransportCompany();
        companyB.setName("ExpressTrans");
        TransportCompanyDao.create(companyB);

        Client bClient = new Client();
        bClient.setFirstName("Nikolay");
        bClient.setLastName("Petkov");
        bClient.setPhone("0888112233");
        ClientDao.create(bClient);

        Driver bDriverNone = new Driver();
        bDriverNone.setFirstName("Radoslav");
        bDriverNone.setLastName("Dimitrov");
        bDriverNone.setSalary(2100);
        bDriverNone.setCompany(companyB);
        EmployeeDao.create(bDriverNone);

        Driver bDriverSpecial = new Driver();
        bDriverSpecial.setFirstName("Kaloyan");
        bDriverSpecial.setLastName("Petrov");
        bDriverSpecial.setSalary(2400);
        bDriverSpecial.setCompany(companyB);
        bDriverSpecial.getQualifications().add(DriverQualification.SPECIAL_CARGO);
        EmployeeDao.create(bDriverSpecial);

        Truck bTruck = new Truck();
        bTruck.setRegistrationNumber("CB3333KK");
        bTruck.setBrand("Volvo");
        bTruck.setModel("FH");
        bTruck.setMaxLoadKg(18000);
        bTruck.setCompany(companyB);
        VehicleDao.create(bTruck);

        Tanker bTanker = new Tanker();
        bTanker.setRegistrationNumber("CB4444LL");
        bTanker.setBrand("MAN");
        bTanker.setModel("TGS");
        bTanker.setMaxLiters(18000);
        bTanker.setFlammable(false); // not flammable
        bTanker.setCompany(companyB);
        VehicleDao.create(bTanker);

        // B1) CargoTransport truck (PAID)
        CargoTransport bCtPaid = new CargoTransport();
        bCtPaid.setCompany(companyB);
        bCtPaid.setClient(bClient);
        bCtPaid.setDriver(bDriverNone);
        bCtPaid.setVehicle(bTruck);
        bCtPaid.setDestination("Burgas");
        bCtPaid.setTransportDate(LocalDate.now().minusDays(10));
        bCtPaid.setPrice(1100);
        bCtPaid.setPaymentStatus(PaymentStatus.PAID);
        bCtPaid.setCargoWeightKg(8600);
        TransportService.createTransport(bCtPaid);

        // B2) CargoTransport tanker not flammable (UNPAID) -> no SPECIAL_CARGO required by our rules
        CargoTransport bCtUnpaid = new CargoTransport();
        bCtUnpaid.setCompany(companyB);
        bCtUnpaid.setClient(bClient);
        bCtUnpaid.setDriver(bDriverNone);
        bCtUnpaid.setVehicle(bTanker);
        bCtUnpaid.setDestination("Ruse");
        bCtUnpaid.setTransportDate(LocalDate.now().minusDays(2));
        bCtUnpaid.setPrice(900);
        bCtUnpaid.setPaymentStatus(PaymentStatus.UNPAID);
        bCtUnpaid.setCargoWeightKg(5000);
        TransportService.createTransport(bCtUnpaid);

        // ===================== Company C =====================
        TransportCompany companyC = new TransportCompany();
        companyC.setName("NorthStar Transport");
        TransportCompanyDao.create(companyC);

        Client cClient = new Client();
        cClient.setFirstName("Stefan");
        cClient.setLastName("Iliev");
        cClient.setPhone("0877009988");
        ClientDao.create(cClient);

        Driver cDriverPassengers = new Driver();
        cDriverPassengers.setFirstName("Yordan");
        cDriverPassengers.setLastName("Ivanov");
        cDriverPassengers.setSalary(2700);
        cDriverPassengers.setCompany(companyC);
        cDriverPassengers.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(cDriverPassengers);

        Bus cBus = new Bus();
        cBus.setRegistrationNumber("CA1010AA");
        cBus.setBrand("Mercedes");
        cBus.setModel("Tourismo");
        cBus.setSeats(49);
        cBus.setCompany(companyC);
        VehicleDao.create(cBus);

        // C1) PassengerTransport (UNPAID)
        PassengerTransport cPtUnpaid = new PassengerTransport();
        cPtUnpaid.setCompany(companyC);
        cPtUnpaid.setClient(cClient);
        cPtUnpaid.setDriver(cDriverPassengers);
        cPtUnpaid.setVehicle(cBus);
        cPtUnpaid.setDestination("Plovdiv");
        cPtUnpaid.setTransportDate(LocalDate.now().minusDays(5));
        cPtUnpaid.setPrice(450);
        cPtUnpaid.setPaymentStatus(PaymentStatus.UNPAID);
        cPtUnpaid.setPassengerCount(20);
        TransportService.createTransport(cPtUnpaid);

        // ===================== FAIL DEMO (not persisted) =====================
        System.out.println("\n--- FAIL DEMO (should throw, NOT persisted) ---");
        try {
            CargoTransport fail = new CargoTransport();
            fail.setCompany(companyA);
            fail.setClient(aClient1);
            fail.setDriver(bDriverNone); // NO SPECIAL_CARGO
            fail.setVehicle(aTanker);    // flammable=true
            fail.setDestination("Vidin");
            fail.setTransportDate(LocalDate.now());
            fail.setPrice(999);
            fail.setPaymentStatus(PaymentStatus.UNPAID);
            fail.setCargoWeightKg(3000);

            TransportService.createTransport(fail);
            System.out.println("FAIL DEMO: Unexpected success (should not happen).");
        } catch (Exception ex) {
            System.out.println("FAIL DEMO: Expected exception -> " + ex.getMessage());
        }

        System.out.println("\nSeed completed.");
    }

    private static void runSortingDemo() {
        System.out.println("\n=== SORTING (Requirement 7) ===");

        System.out.println("\nCompanies sorted by name:");
        TransportCompanyDao.getAllSortedByName()
                .forEach(c -> System.out.println(c.getId() + " " + c.getName()));

        System.out.println("\nCompanies sorted by revenue (PAID only, desc):");
        TransportCompanyDao.getAllSortedByRevenueDesc()
                .forEach(x -> System.out.println(x.getCompanyId() + " " + x.getCompanyName() + " -> " + x.getRevenue()));

        System.out.println("\nDrivers sorted by salary (asc):");
        DriverDao.getDriversSortedBySalaryAsc()
                .forEach(d -> System.out.println(d.getId() + " " + d.getFirstName() + " " + d.getLastName() + " -> " + d.getSalary()));

        System.out.println("\nDrivers with qualification PASSENGERS_OVER_12:");
        DriverDao.getDriversWithQualification(DriverQualification.PASSENGERS_OVER_12)
                .forEach(d -> System.out.println(d.getId() + " " + d.getFirstName() + " " + d.getLastName()));

        System.out.println("\nDrivers with qualification SPECIAL_CARGO:");
        DriverDao.getDriversWithQualification(DriverQualification.SPECIAL_CARGO)
                .forEach(d -> System.out.println(d.getId() + " " + d.getFirstName() + " " + d.getLastName()));

        System.out.println("\nTransports sorted by destination:");
        TransportDao.getAllSortedByDestination()
                .forEach(t -> System.out.println(t.getId() + " " + t.getDestination() + " " + t.getClass().getSimpleName()
                        + " | " + t.getPaymentStatus() + " | " + t.getPrice()));
    }

    private static void runReportsDemo() {
        System.out.println("\n=== REPORTS (Requirement 9) ===");

        System.out.println("Total transports count: " + ReportDao.getTotalTransportsCount());
        System.out.println("Total revenue (PAID only): " + ReportDao.getTotalTransportsRevenue());
        System.out.println("Total services value (PAID + UNPAID): " + ReportDao.getTotalTransportsValue());

        System.out.println("\nDrivers with transports count:");
        ReportDao.getDriversWithTransportsCount()
                .forEach(x -> System.out.println(x.getDriverId() + " " + x.getFirstName() + " " + x.getLastName() + " -> " + x.getTransportsCount()));

        System.out.println("\nDrivers with PAID transports count:");
        ReportDao.getDriversWithPaidTransportsCount()
                .forEach(x -> System.out.println(x.getDriverId() + " " + x.getFirstName() + " " + x.getLastName() + " -> " + x.getPaidTransportsCount()));

        System.out.println("\nRevenue for period (last 30 days, PAID only): " +
                ReportDao.getRevenueForPeriod(LocalDate.now().minusDays(30), LocalDate.now()));

        System.out.println("\nRevenue by driver (PAID only):");
        ReportDao.getRevenueByDriver()
                .forEach(x -> System.out.println(x.getDriverId() + " " + x.getFirstName() + " " + x.getLastName() + " -> " + x.getRevenue()));
    }

    private static void runJsonExportDemo() {
        System.out.println("\n=== JSON EXPORT (Requirement 8) ===");
        String jsonPath = "transports.json";
        TransportJsonService.exportToJson(jsonPath);
        System.out.println("JSON exported to " + jsonPath);
    }
}
