package com.turkishairlines.technology.dt.route_wings.mapper;

import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;

public class TransportationMapper {
    public static TransportationResponseDTO toResponseDTO(Transportation transportation) {
        return TransportationResponseDTO.builder()
                .id(transportation.getId())
                .originName(transportation.getOriginLocation().getName())
                .destinationName(transportation.getDestinationLocation().getName())
                .transportationType(transportation.getTransportationType())
                .operatingDays(transportation.getOperatingDays())
                .build();
    }
}
