package com.turkishairlines.technology.dt.route_wings.controller;

import com.turkishairlines.technology.dt.route_wings.model.location.LocationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @Operation(summary = "Get all locations")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved location list")
    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        List<LocationResponseDTO> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @Operation(summary = "Get location by ID")
    @ApiResponse(responseCode = "200", description = "Location found")
    @ApiResponse(responseCode = "404", description = "Location not found")
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable Long id) {
        LocationResponseDTO location = locationService.getLocationById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @Operation(summary = "Get location by Name")
    @ApiResponse(responseCode = "200", description = "Location found")
    @ApiResponse(responseCode = "404", description = "Location not found")
    @GetMapping(params = "name")
    public ResponseEntity<LocationResponseDTO> getLocationByName(@RequestParam String name) {
        LocationResponseDTO location = locationService.getLocationByName(name);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @Operation(summary = "Create a new location")
    @ApiResponse(responseCode = "201", description = "Location created successfully")
    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody @Valid LocationRequestDTO requestDTO) {
        LocationResponseDTO newLocation = locationService.saveLocation(requestDTO);
        return new ResponseEntity<>(newLocation, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a location by ID")
    @ApiResponse(responseCode = "200", description = "Location updated successfully")
    @PatchMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(@PathVariable Long id, @RequestBody LocationRequestDTO requestDTO) {
        LocationResponseDTO updatedLocation = locationService.updateLocation(id, requestDTO);
        return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
    }

    @Operation(summary = "Delete a location by ID")
    @ApiResponse(responseCode = "204", description = "Location deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
