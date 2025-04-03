package com.turkishairlines.technology.dt.route_wings.model.transportation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private Set<@Min(1) @Max(7) Integer> operatingDays;
}
