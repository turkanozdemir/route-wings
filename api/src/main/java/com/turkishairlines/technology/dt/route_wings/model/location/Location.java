package com.turkishairlines.technology.dt.route_wings.model.location;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Size(min = 3, max = 3, message = "Location code must be exactly 3 characters")
    @Column(nullable = false)
    private String locationCode;
}