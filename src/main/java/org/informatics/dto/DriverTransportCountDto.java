package org.informatics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DriverTransportCountDto {
    private long driverId;
    private String firstName;
    private String lastName;
    private long transportsCount;
}
