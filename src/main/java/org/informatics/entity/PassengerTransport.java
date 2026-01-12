package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transport operation for passengers.
 * Must use a Bus vehicle. If passenger count > 12, driver must have PASSENGERS_OVER_12 qualification.
 */
@Getter
@Setter
@Entity
@Table(name = "passenger_transports")
public class PassengerTransport extends Transport{
    @Column(nullable = false)
    private int passengerCount;
}
