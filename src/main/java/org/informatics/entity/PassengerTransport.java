package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "passenger_transports")
public class PassengerTransport extends Transport{
    @Column(nullable = false)
    private int passengerCount;
}
