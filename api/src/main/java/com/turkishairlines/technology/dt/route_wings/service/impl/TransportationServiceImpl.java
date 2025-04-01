package com.turkishairlines.technology.dt.route_wings.service.impl;

import com.turkishairlines.technology.dt.route_wings.constant.LocationConstants;
import com.turkishairlines.technology.dt.route_wings.constant.TransportationConstants;
import com.turkishairlines.technology.dt.route_wings.exception.NotFoundException;
import com.turkishairlines.technology.dt.route_wings.mapper.TransportationMapper;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.repository.LocationRepository;
import com.turkishairlines.technology.dt.route_wings.repository.TransportationRepository;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<TransportationResponseDTO> getAllTransportations() {
        return transportationRepository.findAll().stream()
                .map(TransportationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransportationResponseDTO getTransportationById(Long id) {
        Transportation transportation = transportationRepository.findById(id).orElseThrow(() -> new NotFoundException(TransportationConstants.TRANSPORTATION, id));
        return TransportationMapper.toResponseDTO(transportation);
    }

    @Override
    public TransportationResponseDTO saveTransportation(TransportationRequestDTO requestDTO) {
        Location origin = locationRepository.findById(requestDTO.getOriginId())
                .orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, requestDTO.getOriginId()));
        Location destination = locationRepository.findById(requestDTO.getDestinationId())
                .orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, requestDTO.getDestinationId()));

        Transportation transportation = transportationRepository.save(TransportationMapper.toEntity(requestDTO, origin, destination));
        return TransportationMapper.toResponseDTO(transportationRepository.save(transportation));
    }

    @Override
    public TransportationResponseDTO updateTransportation(Long id, TransportationRequestDTO requestDTO) {
        Transportation updatedTransportation = transportationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TransportationConstants.TRANSPORTATION, id));

        if (requestDTO.getOriginId() != null) {
            Location origin = locationRepository.findById(requestDTO.getOriginId())
                    .orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, requestDTO.getOriginId()));
            updatedTransportation.setOriginLocation(origin);
        }

        if (requestDTO.getDestinationId() != null) {
            Location destination = locationRepository.findById(requestDTO.getDestinationId())
                    .orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, requestDTO.getDestinationId()));
            updatedTransportation.setDestinationLocation(destination);
        }

        if (requestDTO.getTransportationType() != null) {
            updatedTransportation.setTransportationType(requestDTO.getTransportationType());
        }

        if (requestDTO.getOperatingDays() != null) {
            updatedTransportation.setOperatingDays(requestDTO.getOperatingDays());
        }

        return TransportationMapper.toResponseDTO(transportationRepository.save(updatedTransportation));
    }

    @Override
    public void deleteTransportation(Long id) {
        if (!transportationRepository.existsById(id)) {
            throw new NotFoundException(TransportationConstants.TRANSPORTATION, id);
        }
        transportationRepository.deleteById(id);
    }

    @Override
    public boolean isTransportationAvailableOnDate(Long id, LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        Transportation transportation = transportationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TransportationConstants.TRANSPORTATION, id));
        return transportation.getOperatingDays().contains(dayOfWeek);
    }
}
