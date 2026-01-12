# Documentation: Transport Company Management System

## Project Description

**Transport Company Management System** is a Java application for managing a transport company. The system allows management of companies, clients, employees, vehicles, and transport operations with business rule validation.

---

## Technologies

- **Java 21** - Programming language
- **Hibernate ORM 7.1** - Object-Relational Mapping
- **MySQL 8.0+** - Relational database
- **Gradle 9.0** - Build automation
- **JUnit 5** - Unit and integration tests
- **Mockito 5.8** - Mocking framework
- **Lombok** - Boilerplate reduction

---

## Architecture

The project follows a **layered architecture**:
```
Main (Presentation)
    ↓
Service Layer (Business Logic, Validation)
    ↓
DTO Layer (Data Transfer, Aggregated Results)
    ↓
DAO Layer (Data Access, CRUD)
    ↓
Entity Layer (Domain Objects, Hibernate Mappings)
    ↓
Hibernate ORM
    ↓
MySQL Database
```

**SOLID Principles:**

1. **Single Responsibility (SRP)** - Each class has one responsibility
2. **Open/Closed (OCP)** - Open for extension, closed for modification
3. **Liskov Substitution (LSP)** - Subtypes can replace base types
4. **Interface Segregation (ISP)** - No unnecessary methods
5. **Dependency Inversion (DIP)** - Depend on abstractions

---

## Features

### 1. CRUD operations for transport companies
- Create, read, update, delete companies
- Classes: `TransportCompanyDao`

### 2. CRUD operations for clients
- Client management with: first name, last name, phone
- Classes: `ClientDao`, `Client`

### 3. CRUD operations for vehicles
- **Types:** Bus, Truck, Tanker
- Each vehicle has: registration number, brand, model
- Classes: `VehicleDao`, `Bus`, `Truck`, `Tanker`

### 4. CRUD operations for employees
- **Drivers** with qualifications:
    - `PASSENGERS_OVER_12` - for transporting > 12 passengers
    - `SPECIAL_CARGO` - for transporting flammable cargo
- Classes: `EmployeeDao`, `Driver`

### 5. Recording transports
- **PassengerTransport** - passenger transport (passenger count)
- **CargoTransport** - cargo transport (weight in kg)
- Each transport contains: company, client, driver, vehicle, destination, date, price, payment status
- Classes: `TransportService`, `Transport`, `PassengerTransport`, `CargoTransport`

### 6. Payment management
- Statuses: `PAID`, `UNPAID`
- Method: `TransportDao.setPaymentStatus()`

### 7. Sorting and filtering
- **Companies:** by name, by revenue
- **Drivers:** by salary, by qualification
- **Transports:** by destination

### 8. JSON export
- Export all transports: `TransportJsonService.exportToJson()`
- Read from file: `TransportJsonService.readJson()`

### 9. Reports and statistics
- Total number of transports
- Total revenue from paid transports
- Transport count per driver
- Revenue for period
- Revenue per driver
- Classes: `ReportDao`

---

## Business Rules

The system validates the following rules before saving:

1. **PassengerTransport** must use a **Bus**
2. Transporting **> 12 passengers** requires `PASSENGERS_OVER_12` qualification
3. **CargoTransport** must use a **Truck** or **Tanker**
4. Transporting **flammable cargo** (Tanker with `flammable=true`) requires `SPECIAL_CARGO` qualification
5. All fields (company, client, driver, vehicle) are required

On violation, an exception is thrown and the transport is **NOT saved** to the database.

**Custom Exceptions:**
- `InvalidVehicleForTransportException`
- `DriverQualificationException`
- `MissingRequiredDataException`
- `EntityNotFoundException`

---

## Database

**Inheritance strategy:** JOINED (each class has its own table)

**Main tables:**
- `transport_companies` - companies
- `clients` - clients
- `employees` → `drivers` - employees and drivers
- `vehicles` → `buses`, `trucks`, `tankers` - vehicles
- `transports` → `passenger_transports`, `cargo_transports` - transports

**Relations:**
- TransportCompany (1) → (*) Employee
- TransportCompany (1) → (*) Vehicle
- Transport (1) → (1) Company, Client, Driver, Vehicle

---

## Testing

### Unit Tests (14 tests)
- **Location:** `src/test/java/`
- **Characteristics:** No database, uses Mockito mock objects
- **Goal:** Testing business logic

### Integration Tests (12 tests)
- **Location:** `src/integrationTest/java/`
- **Characteristics:** Uses real MySQL database
- **Goal:** Testing full chain (Service → DAO → DB)

**Running:**
```bash
./gradlew test               # Unit tests
./gradlew integrationTest    # Integration tests
```

---

## Installation and Running

### Prerequisites
- Java 21+
- MySQL 8.0+

### Configuration
Edit `src/main/resources/hibernate.properties`:
```properties
hibernate.connection.username=YOUR_USERNAME
hibernate.connection.password=YOUR_PASSWORD
```

### Running
```bash
./gradlew run
```

Or in IntelliJ IDEA: Right click on `Main.java` → Run

---

## Demo

The application automatically creates 3 companies with data and demonstrates:
- Creating transports
- Business rule validation
- Sorting and filtering
- Generating reports
- Exporting to JSON file (`transports.json`)

---

## Author

**Name:** Aleks Dimitrov  
**University:** New Bulgarian University  
**Course:** Application Programming with Java  
**Date:** January 2025