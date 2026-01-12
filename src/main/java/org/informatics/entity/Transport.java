package org.informatics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.informatics.entity.enums.PaymentStatus;

import java.time.LocalDate;

/**
 * Base class for all transport operations.
 * Uses JOINED inheritance strategy - each transport type has its own table.
 * Transport represents a single service performed by the company.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "transports")
public class Transport extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private TransportCompany company;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDate transportDate;

    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
}
