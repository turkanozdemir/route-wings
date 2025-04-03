package com.turkishairlines.technology.dt.route_wings.model.transportation;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "transportations",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"origin_location", "destination_location", "transportation_type"}))

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transportation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Location originLocation;

    @ManyToOne(optional = false)
    private Location destinationLocation;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransportationType transportationType;

    @ElementCollection
    @CollectionTable(name = "transportation_operating_days", joinColumns = @JoinColumn(name = "transportation_id"))
    @NotEmpty
    private Set<@Min(1) @Max(7) Integer> operatingDays;
}
