package com.turkishairlines.technology.dt.route_wings.controller;

import com.turkishairlines.technology.dt.route_wings.model.Location;
import com.turkishairlines.technology.dt.route_wings.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Location location = locationService.getLocationById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Location> getLocationById(@PathVariable String name) {
        Location location = locationService.getLocationByName(name);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody @Valid Location location) {
        Location newLocation = locationService.saveLocation(location);
        return new ResponseEntity<>(newLocation, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Location> updateLocation(@RequestBody Location location) {
        Location updatedLocation = locationService.getLocationById(location.getId());

        updatedLocation.setName(location.getName());
        updatedLocation.setCountry(location.getCountry());
        updatedLocation.setCity(location.getCity());
        updatedLocation.setLocationCode(location.getLocationCode());

        locationService.saveLocation(updatedLocation);

        return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
