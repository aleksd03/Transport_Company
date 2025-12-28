package org.informatics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transport_companies")
public class TransportCompany extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}
