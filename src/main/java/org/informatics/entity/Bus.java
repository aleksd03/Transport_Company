package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a bus vehicle used for passenger transport.
 */
@Getter
@Setter
@Entity
@Table(name = "buses")
public class Bus extends Vehicle{
    @Column(nullable = false)
    private int seats;
}
