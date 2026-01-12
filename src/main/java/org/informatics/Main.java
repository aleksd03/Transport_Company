package org.informatics;

import org.informatics.dao.*;
import org.informatics.entity.*;
import org.informatics.entity.enums.DriverQualification;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.service.TransportJsonService;
import org.informatics.service.TransportService;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Main demonstration class for Transport Company Management System.
 *
 * This application demonstrates:
 * - CRUD operations for companies, clients, employees, and vehicles
 * - Transport creation with business rule validation
 * - Sorting and filtering capabilities
 * - Report generation
 * - JSON export functionality
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcomeMessage();

        // Step 1: Seed demo data
        seedDemoData();
        pauseForDemo("Press Enter to see SORTING demonstrations...");

        // Step 2: Demonstrate sorting
        runSortingDemo();
        pauseForDemo("Press Enter to see REPORTS...");

        // Step 3: Demonstrate reports
        runReportsDemo();
        pauseForDemo("Press Enter to export data to JSON...");

        // Step 4: Export to JSON
        runJsonExportDemo();

        // Step 5: Show final statistics
        printFinalStatistics();

        printGoodbyeMessage();
    }

    // ========== WELCOME & GOODBYE ==========

    private static void printWelcomeMessage() {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     TRANSPORT COMPANY MANAGEMENT SYSTEM - DEMO                ║");
        System.out.println("║     Student: Aleks Dimitrov                                   ║");
        System.out.println("║     Project: Java Application Programming                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printGoodbyeMessage() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     DEMO COMPLETED SUCCESSFULLY!                              ║");
        System.out.println("║     All functionalities demonstrated.                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    // ========== SEED DATA ==========

    private static void seedDemoData() {
        printSectionHeader("SEED DEMO DATA (3 Companies)");
        System.out.println("Creating companies, clients, employees, vehicles, and transports...\n");

        // ===================== Company A: Alvas Logistics =====================
        System.out.println("Creating Company A: Alvas Logistics");

        TransportCompany companyA = new TransportCompany();
        companyA.setName("Alvas Logistics");
        TransportCompanyDao.create(companyA);
        System.out.println("Company created: " + companyA.getName());

        Client aClient1 = new Client();
        aClient1.setFirstName("Maria");
        aClient1.setLastName("Georgieva");
        aClient1.setPhone("0899001122");
        ClientDao.create(aClient1);
        System.out.println("Client created: " + aClient1.getFirstName() + " " + aClient1.getLastName());

        Client aClient2 = new Client();
        aClient2.setFirstName("Hristo");
        aClient2.setLastName("Kostov");
        aClient2.setPhone("0877554433");
        ClientDao.create(aClient2);
        System.out.println("Client created: " + aClient2.getFirstName() + " " + aClient2.getLastName());

        Driver aDriverAll = new Driver();
        aDriverAll.setFirstName("Daniel");
        aDriverAll.setLastName("Kolev");
        aDriverAll.setSalary(3200);
        aDriverAll.setCompany(companyA);
        aDriverAll.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        aDriverAll.getQualifications().add(DriverQualification.SPECIAL_CARGO);
        EmployeeDao.create(aDriverAll);
        System.out.println("Driver created: " + aDriverAll.getFirstName() + " " + aDriverAll.getLastName()
                + " (Qualifications: PASSENGERS_OVER_12, SPECIAL_CARGO)");

        Driver aDriverPassengers = new Driver();
        aDriverPassengers.setFirstName("Elena");
        aDriverPassengers.setLastName("Stoyanova");
        aDriverPassengers.setSalary(2800);
        aDriverPassengers.setCompany(companyA);
        aDriverPassengers.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(aDriverPassengers);
        System.out.println("Driver created: " + aDriverPassengers.getFirstName() + " " + aDriverPassengers.getLastName()
                + " (Qualification: PASSENGERS_OVER_12)");

        Bus aBus = new Bus();
        aBus.setRegistrationNumber("CA7777TT");
        aBus.setBrand("Setra");
        aBus.setModel("S 516 HD");
        aBus.setSeats(55);
        aBus.setCompany(companyA);
        VehicleDao.create(aBus);
        System.out.println("Bus created: " + aBus.getRegistrationNumber() + " - " + aBus.getBrand() + " " + aBus.getModel());

        Tanker aTanker = new Tanker();
        aTanker.setRegistrationNumber("PB9090PP");
        aTanker.setBrand("Scania");
        aTanker.setModel("R 500");
        aTanker.setMaxLiters(24000);
        aTanker.setFlammable(true);
        aTanker.setCompany(companyA);
        VehicleDao.create(aTanker);
        System.out.println("Tanker created: " + aTanker.getRegistrationNumber() + " - " + aTanker.getBrand()
                + " " + aTanker.getModel() + " (Flammable)");

        // Transport A1: PassengerTransport (PAID)
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
        System.out.println("Transport created: PassengerTransport to Sofia (40 passengers, PAID, 600 BGN)");

        // Transport A2: CargoTransport tanker flammable (UNPAID)
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
        System.out.println("Transport created: CargoTransport to Varna (12000 kg, UNPAID, 2500 BGN)");

        System.out.println();

        // ===================== Company B: ExpressTrans =====================
        System.out.println("Creating Company B: ExpressTrans");

        TransportCompany companyB = new TransportCompany();
        companyB.setName("ExpressTrans");
        TransportCompanyDao.create(companyB);
        System.out.println("Company created: " + companyB.getName());

        Client bClient = new Client();
        bClient.setFirstName("Nikolay");
        bClient.setLastName("Petkov");
        bClient.setPhone("0888112233");
        ClientDao.create(bClient);
        System.out.println("Client created: " + bClient.getFirstName() + " " + bClient.getLastName());

        Driver bDriverNone = new Driver();
        bDriverNone.setFirstName("Radoslav");
        bDriverNone.setLastName("Dimitrov");
        bDriverNone.setSalary(2100);
        bDriverNone.setCompany(companyB);
        EmployeeDao.create(bDriverNone);
        System.out.println("Driver created: " + bDriverNone.getFirstName() + " " + bDriverNone.getLastName()
                + " (No special qualifications)");

        Driver bDriverSpecial = new Driver();
        bDriverSpecial.setFirstName("Kaloyan");
        bDriverSpecial.setLastName("Petrov");
        bDriverSpecial.setSalary(2400);
        bDriverSpecial.setCompany(companyB);
        bDriverSpecial.getQualifications().add(DriverQualification.SPECIAL_CARGO);
        EmployeeDao.create(bDriverSpecial);
        System.out.println("Driver created: " + bDriverSpecial.getFirstName() + " " + bDriverSpecial.getLastName()
                + " (Qualification: SPECIAL_CARGO)");

        Truck bTruck = new Truck();
        bTruck.setRegistrationNumber("CB3333KK");
        bTruck.setBrand("Volvo");
        bTruck.setModel("FH");
        bTruck.setMaxLoadKg(18000);
        bTruck.setCompany(companyB);
        VehicleDao.create(bTruck);
        System.out.println("Truck created: " + bTruck.getRegistrationNumber() + " - " + bTruck.getBrand()
                + " " + bTruck.getModel());

        Tanker bTanker = new Tanker();
        bTanker.setRegistrationNumber("CB4444LL");
        bTanker.setBrand("MAN");
        bTanker.setModel("TGS");
        bTanker.setMaxLiters(18000);
        bTanker.setFlammable(false);
        bTanker.setCompany(companyB);
        VehicleDao.create(bTanker);
        System.out.println("Tanker created: " + bTanker.getRegistrationNumber() + " - " + bTanker.getBrand()
                + " " + bTanker.getModel() + " (Non-flammable)");

        // Transport B1: CargoTransport truck (PAID)
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
        System.out.println("Transport created: CargoTransport to Burgas (8600 kg, PAID, 1100 BGN)");

        // Transport B2: CargoTransport tanker not flammable (UNPAID)
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
        System.out.println("Transport created: CargoTransport to Ruse (5000 kg, UNPAID, 900 BGN)");

        System.out.println();

        // ===================== Company C: NorthStar Transport =====================
        System.out.println("Creating Company C: NorthStar Transport");

        TransportCompany companyC = new TransportCompany();
        companyC.setName("NorthStar Transport");
        TransportCompanyDao.create(companyC);
        System.out.println("Company created: " + companyC.getName());

        Client cClient = new Client();
        cClient.setFirstName("Stefan");
        cClient.setLastName("Iliev");
        cClient.setPhone("0877009988");
        ClientDao.create(cClient);
        System.out.println("Client created: " + cClient.getFirstName() + " " + cClient.getLastName());

        Driver cDriverPassengers = new Driver();
        cDriverPassengers.setFirstName("Yordan");
        cDriverPassengers.setLastName("Ivanov");
        cDriverPassengers.setSalary(2700);
        cDriverPassengers.setCompany(companyC);
        cDriverPassengers.getQualifications().add(DriverQualification.PASSENGERS_OVER_12);
        EmployeeDao.create(cDriverPassengers);
        System.out.println("Driver created: " + cDriverPassengers.getFirstName() + " " + cDriverPassengers.getLastName()
                + " (Qualification: PASSENGERS_OVER_12)");

        Bus cBus = new Bus();
        cBus.setRegistrationNumber("CA1010AA");
        cBus.setBrand("Mercedes");
        cBus.setModel("Tourismo");
        cBus.setSeats(49);
        cBus.setCompany(companyC);
        VehicleDao.create(cBus);
        System.out.println("Bus created: " + cBus.getRegistrationNumber() + " - " + cBus.getBrand()
                + " " + cBus.getModel());

        // Transport C1: PassengerTransport (UNPAID)
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
        System.out.println("Transport created: PassengerTransport to Plovdiv (20 passengers, UNPAID, 450 BGN)");

        System.out.println();

        // ===================== FAIL DEMO =====================
        System.out.println("BUSINESS RULE VALIDATION DEMO (Expected Failure)");
        System.out.println("Attempting to create transport with flammable cargo but unqualified driver...");

        try {
            CargoTransport fail = new CargoTransport();
            fail.setCompany(companyA);
            fail.setClient(aClient1);
            fail.setDriver(bDriverNone); // NO SPECIAL_CARGO qualification
            fail.setVehicle(aTanker);    // flammable=true
            fail.setDestination("Vidin");
            fail.setTransportDate(LocalDate.now());
            fail.setPrice(999);
            fail.setPaymentStatus(PaymentStatus.UNPAID);
            fail.setCargoWeightKg(3000);

            TransportService.createTransport(fail);
            System.out.println("UNEXPECTED: Transport was created (should have failed!)");
        } catch (Exception ex) {
            System.out.println("EXPECTED: " + ex.getMessage());
            System.out.println("-> Transport was NOT persisted to database (business rule enforced)");
        }

        System.out.println("\nSeed completed successfully!\n");
    }

    // ========== SORTING DEMO ==========

    private static void runSortingDemo() {
        printSectionHeader("SORTING & FILTERING (Requirement 7)");

        System.out.println("Companies sorted by NAME (alphabetically):");
        System.out.println("   " + "─".repeat(60));
        TransportCompanyDao.getAllSortedByName()
                .forEach(c -> System.out.println("   ID: " + c.getId() + " | Name: " + c.getName()));
        System.out.println();

        System.out.println("Companies sorted by REVENUE (PAID transports only, descending):");
        System.out.println("   " + "─".repeat(60));
        TransportCompanyDao.getAllSortedByRevenueDesc()
                .forEach(x -> System.out.printf("   ID: %d | %-25s | Revenue: %.2f BGN%n",
                        x.getCompanyId(), x.getCompanyName(), x.getRevenue()));
        System.out.println();

        System.out.println("Drivers sorted by SALARY (ascending):");
        System.out.println("   " + "─".repeat(60));
        DriverDao.getDriversSortedBySalaryAsc()
                .forEach(d -> System.out.printf("   ID: %d | %-20s | Salary: %.2f BGN%n",
                        d.getId(), d.getFirstName() + " " + d.getLastName(), d.getSalary()));
        System.out.println();

        System.out.println("Drivers with qualification: PASSENGERS_OVER_12");
        System.out.println("   " + "─".repeat(60));
        DriverDao.getDriversWithQualification(DriverQualification.PASSENGERS_OVER_12)
                .forEach(d -> System.out.println("   ID: " + d.getId() + " | " + d.getFirstName() + " " + d.getLastName()));
        System.out.println();

        System.out.println("Drivers with qualification: SPECIAL_CARGO");
        System.out.println("   " + "─".repeat(60));
        DriverDao.getDriversWithQualification(DriverQualification.SPECIAL_CARGO)
                .forEach(d -> System.out.println("   ID: " + d.getId() + " | " + d.getFirstName() + " " + d.getLastName()));
        System.out.println();

        System.out.println("Transports sorted by DESTINATION (alphabetically):");
        System.out.println("   " + "─".repeat(60));
        TransportDao.getAllSortedByDestination()
                .forEach(t -> System.out.printf("   ID: %d | %-15s | Type: %-20s | Status: %-6s | Price: %.2f BGN%n",
                        t.getId(), t.getDestination(), t.getClass().getSimpleName(),
                        t.getPaymentStatus(), t.getPrice()));
        System.out.println();
    }

    // ========== REPORTS DEMO ==========

    private static void runReportsDemo() {
        printSectionHeader("REPORTS & STATISTICS (Requirement 9)");

        System.out.println("General Statistics:");
        System.out.println("   " + "─".repeat(60));
        System.out.println("Total transports count: " + ReportDao.getTotalTransportsCount());
        System.out.printf("Total revenue (PAID only): %.2f BGN%n", ReportDao.getTotalTransportsRevenue());
        System.out.printf("Total services value (PAID + UNPAID): %.2f BGN%n", ReportDao.getTotalTransportsValue());
        System.out.println();

        System.out.println("Drivers with transport counts:");
        System.out.println("   " + "─".repeat(60));
        ReportDao.getDriversWithTransportsCount()
                .forEach(x -> System.out.printf("   ID: %d | %-20s | Transports: %d%n",
                        x.getDriverId(), x.getFirstName() + " " + x.getLastName(), x.getTransportsCount()));
        System.out.println();

        System.out.println("Drivers with PAID transport counts:");
        System.out.println("   " + "─".repeat(60));
        ReportDao.getDriversWithPaidTransportsCount()
                .forEach(x -> System.out.printf("   ID: %d | %-20s | Paid transports: %d%n",
                        x.getDriverId(), x.getFirstName() + " " + x.getLastName(), x.getPaidTransportsCount()));
        System.out.println();

        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();
        System.out.printf("Revenue for period (%s to %s, PAID only): %.2f BGN%n",
                from, to, ReportDao.getRevenueForPeriod(from, to));
        System.out.println();

        System.out.println("Revenue by driver (PAID transports only):");
        System.out.println("   " + "─".repeat(60));
        ReportDao.getRevenueByDriver()
                .forEach(x -> System.out.printf("   ID: %d | %-20s | Revenue: %.2f BGN%n",
                        x.getDriverId(), x.getFirstName() + " " + x.getLastName(), x.getRevenue()));
        System.out.println();
    }

    // ========== JSON EXPORT DEMO ==========

    private static void runJsonExportDemo() {
        printSectionHeader("JSON EXPORT (Requirement 8)");

        String jsonPath = "transports.json";
        System.out.println("Exporting all transports to JSON file...");
        System.out.println("File path: " + jsonPath);

        TransportJsonService.exportToJson(jsonPath);

        System.out.println("JSON export completed successfully!");
        System.out.println("You can open the file to see all transport data.");
        System.out.println();
    }

    // ========== FINAL STATISTICS ==========

    private static void printFinalStatistics() {
        printSectionHeader("FINAL SYSTEM STATISTICS");

        System.out.println("Database Summary:");
        System.out.println("   " + "─".repeat(60));
        System.out.println("Total Companies: " + TransportCompanyDao.getAll().size());
        System.out.println("Total Clients: " + ClientDao.getAll().size());
        System.out.println("Total Employees: " + EmployeeDao.getAll().size());
        System.out.println("Total Drivers: " + DriverDao.getAllDrivers().size());
        System.out.println("Total Vehicles: " + VehicleDao.getAll().size());
        System.out.println("Total Transports: " + TransportDao.getAll().size());
        System.out.println();

        System.out.println("Business Metrics:");
        System.out.println("   " + "─".repeat(60));
        System.out.printf("Total Revenue (PAID): %.2f BGN%n", ReportDao.getTotalTransportsRevenue());
        System.out.printf("Total Value (ALL): %.2f BGN%n", ReportDao.getTotalTransportsValue());
        System.out.printf("Payment Rate: %.1f%%%n",
                (ReportDao.getTotalTransportsRevenue() / ReportDao.getTotalTransportsValue() * 100));
        System.out.println();
    }

    // ========== HELPER METHODS ==========

    /**
     * Prints a formatted section header.
     */
    private static void printSectionHeader(String title) {
        System.out.println("═".repeat(70));
        System.out.println("  " + title);
        System.out.println("═".repeat(70));
        System.out.println();
    }

    /**
     * Pauses execution and waits for user input (for demo purposes).
     */
    private static void pauseForDemo(String message) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println(message);
        System.out.println("─".repeat(70));
        scanner.nextLine();
        System.out.println();
    }
}
