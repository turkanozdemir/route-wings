package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.model.Location;

import java.util.List;

public interface LocationService {
    List<Location> getAllLocations();

    Location getLocationById(Long id);

    Location getLocationByName(String name);

    Location saveLocation(Location location);

    void deleteLocationById(Long id);
}
