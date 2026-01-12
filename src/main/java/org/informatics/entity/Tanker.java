package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a tanker vehicle used for liquid cargo transport.
 */
@Getter
@Setter
@Entity
@Table(name = "tankers")
public class Tanker extends Vehicle{
    @Column(nullable = false)
    private double maxLiters;

    /**
     * Indicates whether the tanker transports flammable cargo.
     * If true, the driver must have SPECIAL_CARGO qualification.
     */
    @Column(nullable = false)
    private boolean flammable;
}
