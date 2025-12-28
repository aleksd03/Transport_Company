package org.informatics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyRevenueDto {
    private long companyId;
    private String companyName;
    private double revenue;
}
