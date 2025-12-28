# Transport Company Management System
## Application Description (English)

This application is a Transport Company Management System developed in Java using object-oriented principles and a relational database. Its main purpose is to support and simplify the management of transportation activities by providing functionality for handling clients, employees, vehicles, and transport operations.

The system supports different types of transport services, including passenger transport and cargo transport, and enforces business rules that ensure the correctness and safety of each operation. Every transport is defined by a destination, transport date, price, and payment status.

Transport companies can be created and managed within the system, along with their associated employees and vehicles. Clients can be registered and linked to specific transport operations. Drivers may have specific qualifications that are required for certain transport types, such as transporting more than 12 passengers or carrying special or flammable cargo.

When creating a transport operation, the system automatically validates all business rules. Passenger transport can only be performed using a bus, and transporting more than 12 passengers requires a specific driver qualification. Cargo transport can be carried out using a truck or a tanker, and transporting flammable cargo requires a special driver qualification. Any violation of these rules results in a custom exception, preventing invalid data from being persisted.

The application provides functionality for sorting and filtering data based on different criteria, such as sorting transport companies by name or revenue, drivers by salary and qualification, and transports by destination.

In addition, the system generates various reports, including the total number of transports, total revenue from paid transports, total value of all services, revenue for a specific period, and revenue per driver. Revenue calculations are based only on paid transports to reflect actual company income.

Transport data can be exported to an external JSON file, enabling further processing, archiving, or analysis outside the system. The application also includes automated unit tests that verify the correctness of the business logic and validation rules.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Описание на приложението (Български)

Настоящото приложение представлява система за управление на транспортна компания, разработена на Java с използване на обектно-ориентиран подход и релационна база данни. Основната цел на системата е да улесни управлението на дейностите в една транспортна компания, като предоставя възможност за работа с клиенти, служители, превозни средства и транспортни превози.

Приложението поддържа различни типове транспортни услуги, като превоз на пътници и превоз на товари, и осигурява автоматична проверка на бизнес правилата, свързани с тяхното изпълнение. Всеки превоз се характеризира с начална и крайна дестинация, дата на извършване, цена и статус на плащане.

Системата позволява създаване и управление на транспортни компании, към които са асоциирани служители и превозни средства. Клиентите могат да бъдат регистрирани и свързвани с конкретни превози. За служителите от тип „шофьор“ се поддържат квалификации, които са необходими за извършване на определени видове превози, като например превоз на повече от 12 пътника или превоз на специални и запалими товари.

При създаване на превоз се извършва валидация на бизнес правилата. Превоз на пътници може да бъде осъществен само с автобус, а при повече от 12 пътника се изисква съответна квалификация на шофьора. Превоз на товари се извършва с камион или цистерна, като при превоз на запалими товари се изисква специална квалификация. При нарушение на тези правила системата предотвратява записването на невалидни данни чрез специализирани изключения.

Приложението предоставя възможност за сортиране и филтриране на данните по различни критерии, като например сортиране на транспортни компании по име или по приходи, шофьори по заплата и квалификация, както и превози по дестинация.

Освен това системата генерира различни справки, включително общ брой извършени превози, общ приход от платени превози, обща стойност на всички услуги, приходи за определен период от време и приходи по шофьор. Приходите се изчисляват на база само на платените превози, което отразява реалните постъпления на компанията.

Данните за превозите могат да бъдат експортирани във външен JSON файл, което позволява допълнителна обработка, архивиране или анализ извън системата. Приложението включва и автоматизирани тестове, които проверяват коректността на бизнес логиката и валидациите.
