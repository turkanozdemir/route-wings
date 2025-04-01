package com.turkishairlines.technology.dt.route_wings.model.transportation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportationRequestDTO {
    @NotNull
    private Long originId;

    @NotNull
    private Long destinationId;

    @NotNull
    private TransportationType transportationType;

    private Set<Integer> operatingDays;
}
