package org.informatics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TransportJsonDto {
    private long id;
    private String type;
    private LocalDate date;
    private String destination;
    private double price;
    private String paymentStatus;

    private long driverId;
    private String driverName;

    private long vehicleId;
    private String vehicleRegistration;

    private long clientId;
    private String clientName;
}
