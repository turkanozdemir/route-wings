package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;

import java.time.LocalDate;
import java.util.List;

public interface TransportationService {
    List<Transportation> getAllTransportations();

    Transportation getTransportationById(Long id);

    Transportation saveTransportation(Transportation transportation);

    Transportation updateTransportation(Transportation transportation);

    void deleteTransportation(Long id);

    boolean isTransportationAvailableOnDate(Transportation transportation, LocalDate date);
}
