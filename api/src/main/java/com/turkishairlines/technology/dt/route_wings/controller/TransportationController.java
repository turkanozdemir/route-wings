package com.turkishairlines.technology.dt.route_wings.controller;

import com.turkishairlines.technology.dt.route_wings.constant.TransportationConstants;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transportations")
public class TransportationController {

    @Autowired
    private TransportationService transportationService;

    @GetMapping
    public ResponseEntity<List<Transportation>> getAllTransportations() {
        List<Transportation> transportations = transportationService.getAllTransportations();
        return new ResponseEntity<>(transportations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transportation> getTransportationById(@PathVariable Long id) {
        Transportation transportation = transportationService.getTransportationById(id);
        return new ResponseEntity<>(transportation, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transportation> createTransportation(@RequestBody @Valid Transportation transportation) {
        Transportation savedTransportation = transportationService.saveTransportation(transportation);
        return new ResponseEntity<>(savedTransportation, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Transportation> updateTransportation(@RequestBody Transportation transportation) {
        Transportation updatedTransportation = transportationService.updateTransportation(transportation);
        return new ResponseEntity<>(updatedTransportation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportation(@PathVariable Long id) {
        transportationService.deleteTransportation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<String> isTransportationAvailableOnDate(@PathVariable Long id, @RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);

        Transportation transportation = transportationService.getTransportationById(id);
        boolean isAvailable = transportationService.isTransportationAvailableOnDate(transportation, date);

        if (isAvailable) {
            return ResponseEntity.ok(TransportationConstants.TRANSPORTATION_AVAILABLE + date);
        } else {
            return ResponseEntity.ok(TransportationConstants.TRANSPORTATION_NOT_AVAILABLE + date);
        }
    }
}
