package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cargo_transports")
public class CargoTransport extends Transport{
    @Column(nullable = false)
    private double cargoWeightKg;
}
