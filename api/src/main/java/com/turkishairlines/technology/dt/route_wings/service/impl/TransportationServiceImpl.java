package com.turkishairlines.technology.dt.route_wings.service.impl;

import com.turkishairlines.technology.dt.route_wings.constant.TransportationConstants;
import com.turkishairlines.technology.dt.route_wings.exception.NotFoundException;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.repository.TransportationRepository;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TransportationServiceImpl implements TransportationService {

    @Autowired
    private TransportationRepository transportationRepository;

    @Override
    public List<Transportation> getAllTransportations() {
        return transportationRepository.findAll();
    }

    @Override
    public Transportation getTransportationById(Long id) {
        return transportationRepository.findById(id).orElseThrow(() -> new NotFoundException(TransportationConstants.TRANSPORTATION, id));
    }

    @Override
    public Transportation saveTransportation(Transportation transportation) {
        return transportationRepository.save(transportation);
    }

    @Override
    public Transportation updateTransportation(Transportation transportation) {
        Transportation updatedTransportation = getTransportationById(transportation.getId());

        updatedTransportation.setOriginLocation(transportation.getOriginLocation());
        updatedTransportation.setDestinationLocation(transportation.getDestinationLocation());
        updatedTransportation.setTransportationType(transportation.getTransportationType());
        updatedTransportation.setOperatingDays(transportation.getOperatingDays());

        return transportationRepository.save(updatedTransportation);
    }

    @Override
    public void deleteTransportation(Long id) {
        if (!transportationRepository.existsById(id)) {
            throw new NotFoundException(TransportationConstants.TRANSPORTATION, id);
        }
        transportationRepository.deleteById(id);
    }

    @Override
    public boolean isTransportationAvailableOnDate(Transportation transportation, LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return transportation.getOperatingDays().contains(dayOfWeek);
    }
}
