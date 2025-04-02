package com.turkishairlines.technology.dt.route_wings.model.route;

import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDTO {
    private List<TransportationResponseDTO> steps;
}
