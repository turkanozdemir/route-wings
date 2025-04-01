package com.turkishairlines.technology.dt.route_wings.mapper;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationResponseDTO;

public class LocationMapper {
    public static Location toEntity(LocationRequestDTO dto) {
        return Location.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .country(dto.getCountry())
                .locationCode(dto.getLocationCode())
                .build();
    }

    public static LocationResponseDTO toResponseDTO(Location location) {
        return LocationResponseDTO.builder()
                .id(location.getId())
                .name(location.getName())
                .city(location.getCity())
                .country(location.getCountry())
                .locationCode(location.getLocationCode())
                .build();
    }
}
