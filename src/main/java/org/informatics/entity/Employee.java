package org.informatics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(exclude = {"company"})
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "employees")
public class Employee extends BaseEntity {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private double salary;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private TransportCompany company;
}
