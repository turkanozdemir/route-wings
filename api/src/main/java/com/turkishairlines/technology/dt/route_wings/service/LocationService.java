package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationResponseDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface LocationService {
    List<LocationResponseDTO> getAllLocations();

    LocationResponseDTO getLocationById(Long id);

    LocationResponseDTO getLocationByName(String name);

    LocationResponseDTO saveLocation(LocationRequestDTO requestDTO);

    LocationResponseDTO updateLocation(Long id, LocationRequestDTO requestDTO);

    void deleteLocationById(Long id);

    Location getLocationEntityById(@NotNull Long originId);

    Location getByLocationCode(String locationCode);
}
