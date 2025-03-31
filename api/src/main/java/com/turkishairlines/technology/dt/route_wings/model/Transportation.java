package com.turkishairlines.technology.dt.route_wings.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transportations")
public class Transportation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Location originLocation;

    @ManyToOne(optional = false)
    private Location destinationLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportationType transportationType;

    @ElementCollection
    @CollectionTable(name = "transportation_operating_days", joinColumns = @JoinColumn(name = "transportation_id"))
    private Set<@Min(1) @Max(7) Integer> operatingDays;
}
