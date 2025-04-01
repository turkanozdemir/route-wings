package com.turkishairlines.technology.dt.route_wings.controller;

import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transportations")
@RequiredArgsConstructor
public class TransportationController {
    private final TransportationService transportationService;

    @Operation(summary = "Get all transportations")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved transpotation list")
    @GetMapping
    public ResponseEntity<List<TransportationResponseDTO>> getAllTransportations() {
        List<TransportationResponseDTO> transportations = transportationService.getAllTransportations();
        return new ResponseEntity<>(transportations, HttpStatus.OK);
    }

    @Operation(summary = "Get transportation by ID")
    @ApiResponse(responseCode = "200", description = "Transportation found")
    @ApiResponse(responseCode = "404", description = "Transportation not found")
    @GetMapping("/{id}")
    public ResponseEntity<TransportationResponseDTO> getTransportationById(@PathVariable Long id) {
        TransportationResponseDTO transportation = transportationService.getTransportationById(id);
        return new ResponseEntity<>(transportation, HttpStatus.OK);
    }

    @Operation(summary = "Create a new transportation")
    @ApiResponse(responseCode = "201", description = "Transportation created successfully")
    @PostMapping
    public ResponseEntity<TransportationResponseDTO> createTransportation(@RequestBody @Valid TransportationRequestDTO requestDTO) {
        TransportationResponseDTO newTransportation = transportationService.saveTransportation(requestDTO);
        return new ResponseEntity<>(newTransportation, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a transportation by ID")
    @ApiResponse(responseCode = "200", description = "Transportation updated successfully")
    @PatchMapping("/{id}")
    public ResponseEntity<TransportationResponseDTO> updateTransportation(@PathVariable Long id, @RequestBody TransportationRequestDTO requestDTO) {
        TransportationResponseDTO updatedTransportation = transportationService.updateTransportation(id, requestDTO);
        return new ResponseEntity<>(updatedTransportation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportation(@PathVariable Long id) {
        transportationService.deleteTransportation(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if transportation is available on a specific date")
    @ApiResponse(responseCode = "200", description = "Availability checked successfully")
    @ApiResponse(responseCode = "404", description = "Transportation not found")
    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> isAvailableOnDate(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        boolean isAvailable = transportationService.isTransportationAvailableOnDate(id, date);
        return ResponseEntity.ok(isAvailable);
    }
}
