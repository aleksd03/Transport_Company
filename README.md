# Transport Company Management System

A Java-based transport company management system using OOP principles, Hibernate ORM, and MySQL database.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Hibernate](https://img.shields.io/badge/Hibernate-7.1-green.svg)](https://hibernate.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Gradle](https://img.shields.io/badge/Gradle-9.0-blue.svg)](https://gradle.org/)

## 🚀 Features

- ✅ Full CRUD operations for companies, clients, employees, and vehicles
- ✅ Business rule validation for transport operations
- ✅ Advanced sorting and filtering capabilities
- ✅ Comprehensive reporting system
- ✅ JSON data export functionality
- ✅ Automated testing (14 Unit + 12 Integration tests)

## 🛠️ Technologies

- **Java 21** - Programming language
- **Hibernate ORM 7.1** - Object-Relational Mapping
- **MySQL 8.0+** - Relational database
- **Gradle 9.0** - Build automation
- **JUnit 5** - Testing framework
- **Mockito 5.8** - Mocking framework
- **Lombok** - Boilerplate reduction

## 📋 Requirements

- Java 21 or higher
- MySQL 8.0 or higher
- Gradle 9.0 or higher (or use included wrapper)

## ⚙️ Installation

1. **Clone the repository**
```bash
   git clone https://github.com/aleksd03/Transport_Company.git
   cd Transport_Company
```

2. **Configure database**

   Edit `src/main/resources/hibernate.properties`:
```properties
   hibernate.connection.username=YOUR_USERNAME
   hibernate.connection.password=YOUR_PASSWORD
```

3. **Run the application**
```bash
   ./gradlew run
```

## 🧪 Testing
```bash
# Run unit tests (fast, with mock objects)
./gradlew test

# Run integration tests (with MySQL database)
./gradlew integrationTest

# Run all tests
./gradlew test integrationTest
```

## 📖 Documentation

For detailed documentation, see:
- **English:** [DOCUMENTATION.md](DOCUMENTATION.md)
- **Bulgarian:** [DOCUMENTATION_BG.md](DOCUMENTATION_BG.md)

## 🏗️ Architecture

The project follows a **layered architecture** pattern:
```
Presentation Layer (Main)
         ↓
Service Layer (Business Logic)
         ↓
DTO Layer (Data Transfer Objects)
         ↓
DAO Layer (Data Access)
         ↓
Entity Layer (Domain Objects)
         ↓
Persistence Layer (Hibernate)
         ↓
Database Layer (MySQL)
```

### SOLID Principles

- **Single Responsibility** - Each class has one responsibility
- **Open/Closed** - Open for extension, closed for modification
- **Liskov Substitution** - Subtypes can replace base types
- **Interface Segregation** - No unnecessary methods
- **Dependency Inversion** - Depend on abstractions

## 🎯 Business Rules

The system enforces the following business rules:

1. **Passenger transport** must use a **Bus**
2. Transporting **> 12 passengers** requires `PASSENGERS_OVER_12` qualification
3. **Cargo transport** must use a **Truck** or **Tanker**
4. Transporting **flammable cargo** requires `SPECIAL_CARGO` qualification
5. All required fields (company, client, driver, vehicle) must be present

Violations throw custom exceptions and prevent database persistence.

## 📊 Database Schema

The project uses **JOINED inheritance strategy**:

- `transport_companies` - Companies
- `clients` - Clients
- `employees` → `drivers` - Employees and drivers with qualifications
- `vehicles` → `buses`, `trucks`, `tankers` - Vehicles by type
- `transports` → `passenger_transports`, `cargo_transports` - Transport operations

## 🚀 Demo

Run the application to see:
- Automatic creation of 3 companies with sample data
- Business rule validation (including failure cases)
- Sorting and filtering demonstrations
- Report generation
- JSON export to `transports.json`

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📝 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Aleks Dimitrov**
- University: New Bulgarian University
- Course: Application Programming with Java
- Date: January 2025
- GitHub: [@aleksd03](https://github.com/aleksd03)

## 🙏 Acknowledgments

- New Bulgarian University for the project requirements
- Hibernate and MySQL communities
- Open-source contributors

---

⭐ If you find this project helpful, please consider giving it a star!
```