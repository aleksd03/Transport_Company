package org.informatics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Base class for all vehicles owned by a transport company.
 * Uses JOINED inheritance strategy - each vehicle type has its own table.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(exclude = {"company"})
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String registrationNumber; // напр. CA1234AB

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private TransportCompany company;
}
