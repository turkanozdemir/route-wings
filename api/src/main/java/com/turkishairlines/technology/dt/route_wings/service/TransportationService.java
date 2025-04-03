package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface TransportationService {
    List<TransportationResponseDTO> getAllTransportations();

    TransportationResponseDTO getTransportationById(Long id);

    TransportationResponseDTO saveTransportation(TransportationRequestDTO transportation);

    TransportationResponseDTO updateTransportation(Long id, TransportationRequestDTO transportation);

    void deleteTransportation(Long id);

    boolean isTransportationAvailableOnDate(Long id, LocalDate date);

    boolean isLocationUsed(Location location);

    List<Transportation> getAllByOriginLocation(Location origin);
}
