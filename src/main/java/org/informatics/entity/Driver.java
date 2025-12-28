package org.informatics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.informatics.entity.enums.DriverQualification;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"qualifications"})
@Table(name = "drivers")
public class Driver extends Employee{
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "driver_qualifications", joinColumns = @JoinColumn(name = "driver_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "qualification")
    private Set<DriverQualification> qualifications = new HashSet<>();
}
