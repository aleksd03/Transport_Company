package org.informatics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a transport company that owns vehicles and employs drivers.
 * A company can have multiple employees, vehicles, and transports.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transport_companies")
public class TransportCompany extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}
