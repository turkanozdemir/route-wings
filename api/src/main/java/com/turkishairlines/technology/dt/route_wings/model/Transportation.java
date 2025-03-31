package com.turkishairlines.technology.dt.route_wings.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Integer[] operatingDays;/**/
}
