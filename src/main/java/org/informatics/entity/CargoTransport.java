package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transport operation for cargo.
 * Must use a Truck or Tanker. If using flammable Tanker, driver must have SPECIAL_CARGO qualification.
 */
@Getter
@Setter
@Entity
@Table(name = "cargo_transports")
public class CargoTransport extends Transport{
    @Column(nullable = false)
    private double cargoWeightKg;
}
