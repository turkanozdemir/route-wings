package com.turkishairlines.technology.dt.route_wings.service.impl;

import com.turkishairlines.technology.dt.route_wings.constant.LocationConstants;
import com.turkishairlines.technology.dt.route_wings.exception.AlreadyExistsException;
import com.turkishairlines.technology.dt.route_wings.exception.NotFoundException;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.repository.LocationRepository;
import com.turkishairlines.technology.dt.route_wings.service.LocationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, id));
    }

    @Override
    public Location getLocationByName(String name) {
        return locationRepository.findByName(name).orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, name));
    }

    @Override
    public Location saveLocation(Location location) {
        if (locationRepository.findByName(location.getName()).isPresent()) {
            throw new AlreadyExistsException(LocationConstants.LOCATION, LocationConstants.NAME, location.getName());
        }
        return locationRepository.save(location);
    }

    @Override
    public void deleteLocationById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NotFoundException(LocationConstants.LOCATION, id);
        }
        locationRepository.deleteById(id);
    }
}
