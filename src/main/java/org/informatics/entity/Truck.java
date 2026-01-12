package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a truck vehicle used for cargo transport.
 */
@Getter
@Setter
@Entity
@Table(name = "trucks")
public class Truck extends Vehicle{
    @Column(nullable = false)
    private double maxLoadKg;
}
