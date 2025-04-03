package com.turkishairlines.technology.dt.route_wings.mapper;

import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RouteMapper {

    public RouteDTO toDTO(List<Transportation> route) {
        return RouteDTO.builder()
                .steps(route.stream()
                        .map(TransportationMapper::toResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
