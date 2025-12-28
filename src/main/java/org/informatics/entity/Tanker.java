package org.informatics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tankers")
public class Tanker extends Vehicle{
    @Column(nullable = false)
    private double maxLiters;

    @Column(nullable = false)
    private boolean flammable;
}
