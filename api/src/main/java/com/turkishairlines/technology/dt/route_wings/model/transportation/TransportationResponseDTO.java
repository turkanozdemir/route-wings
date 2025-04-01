package com.turkishairlines.technology.dt.route_wings.model.transportation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportationResponseDTO {
    private Long id;
    private String originName;
    private String destinationName;
    private TransportationType transportationType;
    private Set<Integer> operatingDays;
}
