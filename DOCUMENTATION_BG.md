# Документация: Transport Company Management System

## Описание на проекта

**Transport Company Management System** е Java приложение за управление на транспортна компания. Системата позволява управление на компании, клиенти, служители, превозни средства и транспортни операции с валидация на бизнес правила.

---

## Технологии

- **Java 21** - Основен програмен език
- **Hibernate ORM 7.1** - Object-Relational Mapping
- **MySQL 8.0+** - Релационна база данни
- **Gradle 9.0** - Build automation
- **JUnit 5** - Unit и integration тестове
- **Mockito 5.8** - Mocking framework
- **Lombok** - Намаляване на boilerplate код

---

## Архитектура

Проектът следва **многослойна архитектура**:
```
Main (Presentation)
    ↓
Service Layer (Бизнес логика, Валидация)
    ↓
DTO Layer (Трансфер на данни, Агрегирани резултати)
    ↓
DAO Layer (Достъп до данни, CRUD)
    ↓
Entity Layer (Domain обекти, Hibernate mappings)
    ↓
Hibernate ORM
    ↓
MySQL Database
```

**SOLID Принципи:**

1. **Single Responsibility (SRP)** - Всеки клас има една отговорност
2. **Open/Closed (OCP)** - Отворен за разширение, затворен за модификация
3. **Liskov Substitution (LSP)** - Подкласовете заменят базовите класове
4. **Interface Segregation (ISP)** - Класовете не имплементират ненужни методи
5. **Dependency Inversion (DIP)** - Зависимост от абстракции

---

## Функционалности

### 1. CRUD операции за транспортни компании
- Създаване, четене, редактиране и изтриване на компании
- Класове: `TransportCompanyDao`

### 2. CRUD операции за клиенти
- Управление на клиенти със данни: име, фамилия, телефон
- Класове: `ClientDao`, `Client`

### 3. CRUD операции за превозни средства
- **Типове:** Bus (автобус), Truck (камион), Tanker (цистерна)
- Всяко превозно средство има регистрационен номер, марка, модел
- Класове: `VehicleDao`, `Bus`, `Truck`, `Tanker`

### 4. CRUD операции за служители
- **Шофьори** с квалификации:
  - `PASSENGERS_OVER_12` - за превоз на > 12 пътници
  - `SPECIAL_CARGO` - за превоз на леснозапалими товари
- Класове: `EmployeeDao`, `Driver`

### 5. Записване на превози
- **PassengerTransport** - превоз на пътници (брой пътници)
- **CargoTransport** - превоз на товари (тегло в кг)
- Всеки превоз съдържа: компания, клиент, шофьор, превозно средство, дестинация, дата, цена, статус на плащане
- Класове: `TransportService`, `Transport`, `PassengerTransport`, `CargoTransport`

### 6. Управление на плащания
- Статуси: `PAID` (платено), `UNPAID` (неплатено)
- Метод: `TransportDao.setPaymentStatus()`

### 7. Сортиране и филтриране
- **Компании:** по име, по приходи
- **Шофьори:** по заплата, по квалификация
- **Превози:** по дестинация

### 8. Експорт в JSON
- Експорт на всички превози във файл: `TransportJsonService.exportToJson()`
- Четене от файл: `TransportJsonService.readJson()`

### 9. Справки и отчети
- Общ брой превози
- Обща сума от платени превози
- Брой превози по шофьор
- Приходи за период
- Приходи по шофьор
- Класове: `ReportDao`

---

## Бизнес правила

Системата валидира следните правила преди запис:

1. **PassengerTransport** трябва да използва **Bus**
2. Превоз на **> 12 пътници** изисква квалификация **PASSENGERS_OVER_12**
3. **CargoTransport** трябва да използва **Truck** или **Tanker**
4. Превоз на **леснозапалими товари** (Tanker с `flammable=true`) изисква квалификация **SPECIAL_CARGO**
5. Всички полета (company, client, driver, vehicle) са задължителни

При нарушение се хвърля изключение и превозът **НЕ СЕ записва** в базата данни.

**Custom Exceptions:**
- `InvalidVehicleForTransportException`
- `DriverQualificationException`
- `MissingRequiredDataException`
- `EntityNotFoundException`

---

## База данни

**Inheritance стратегия:** JOINED (всеки клас има собствена таблица)

**Основни таблици:**
- `transport_companies` - компании
- `clients` - клиенти
- `employees` → `drivers` - служители и шофьори
- `vehicles` → `buses`, `trucks`, `tankers` - превозни средства
- `transports` → `passenger_transports`, `cargo_transports` - превози

**Релации:**
- TransportCompany (1) → (*) Employee
- TransportCompany (1) → (*) Vehicle
- Transport (1) → (1) Company, Client, Driver, Vehicle

---

## Тестване

### Unit тестове (14 теста)
- **Локация:** `src/test/java/`
- **Характеристики:** Без база данни, използват Mockito mock обекти
- **Цел:** Тестване на бизнес логика

### Integration тестове (12 теста)
- **Локация:** `src/integrationTest/java/`
- **Характеристики:** Използват реална MySQL база
- **Цел:** Тестване на пълната верига (Service → DAO → DB)

**Пускане:**
```bash
./gradlew test               # Unit тестове
./gradlew integrationTest    # Integration тестове
```

---

## Инсталация и стартиране

### Предварителни изисквания
- Java 21+
- MySQL 8.0+

### Конфигурация
Редактирай `src/main/resources/hibernate.properties`:
```properties
hibernate.connection.username=YOUR_USERNAME
hibernate.connection.password=YOUR_PASSWORD
```

### Стартиране
```bash
./gradlew run
```

Или в IntelliJ IDEA: Right click на `Main.java` → Run

---

## Демонстрация

Приложението автоматично създава 3 компании с данни и демонстрира:
- Създаване на превози
- Валидация на бизнес правила
- Сортиране и филтриране
- Генериране на справки
- Експорт в JSON файл (`transports.json`)

---

## Автор

**Име:** Алекс Димитров  
**Университет:** Нов Български Университет  
**Курс:** Приложно програмиране с Java  
**Дата:** Януари 2025