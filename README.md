# Transport Company Management System

## Application Description (English)

This project is a Transport Company Management System developed in Java using object-oriented principles, Hibernate ORM, and a relational database. The system is designed to manage transportation activities by handling clients, employees, vehicles, and transport operations.

The application supports multiple transport types, including passenger and cargo transport. Each transport operation is defined by destination, transport date, price, and payment status, and is associated with a specific client, driver, vehicle, and transport company.

Business rules are enforced when creating transport operations. Passenger transport can only be performed using a bus, and transporting more than 12 passengers requires a specific driver qualification. Cargo transport can be performed using a truck or a tanker, and transporting special or flammable cargo requires additional driver qualifications. Violations of these rules result in custom exceptions, preventing invalid data from being persisted.

The system provides functionality for sorting data and generating reports, including total number of transports, revenue from paid transports, revenue by driver, and revenue for a specific period. Transport data can also be exported to an external JSON file.

The project includes automated testing, consisting of unit tests for business logic validation and integration tests that verify database interaction using Hibernate. Unit and integration tests are separated into different Gradle tasks and use independent configurations.

---

## Описание на приложението (Български)

Приложението представлява система за управление на транспортна компания, разработена на Java с използване на обектно-ориентиран подход, Hibernate ORM и релационна база данни. Основната цел на системата е да подпомогне управлението на транспортни дейности чрез работа с клиенти, служители, превозни средства и транспортни превози.

Системата поддържа различни типове превози, включително превоз на пътници и превоз на товари. Всеки превоз се характеризира с дестинация, дата, цена и статус на плащане. Превозите се асоциират с конкретен клиент, шофьор, превозно средство и транспортна компания.

При създаване на превоз системата извършва проверка на бизнес правилата. Превоз на пътници може да бъде осъществен само с автобус, а при повече от 12 пътника се изисква специална квалификация на шофьора. Превоз на товари може да се извършва с камион или цистерна, като при превоз на специални или запалими товари се изисква допълнителна квалификация. При нарушение на тези правила превозът не се записва в базата чрез използване на специализирани изключения.

Приложението предоставя възможност за сортиране на данните по различни критерии, както и генериране на справки за броя превози, приходите от платени услуги, приходите по шофьор и за определен период от време. Данните за превозите могат да бъдат експортирани във външен JSON файл.

В проекта са реализирани автоматизирани тестове, включително unit тестове за проверка на бизнес логиката и integration тестове за проверка на работата с базата данни чрез Hibernate. Unit и integration тестовете са разделени като отделни Gradle задачи и използват различни конфигурации.

---

## Technologies Used
- Java
- Hibernate ORM
- MySQL
- Gradle
- JUnit 5
- Lombok
- Log4j
