package com.turkishairlines.technology.dt.route_wings.service.impl;

import com.turkishairlines.technology.dt.route_wings.constant.ErrorMessages;
import com.turkishairlines.technology.dt.route_wings.constant.LocationConstants;
import com.turkishairlines.technology.dt.route_wings.exception.AlreadyExistsException;
import com.turkishairlines.technology.dt.route_wings.exception.NotFoundException;
import com.turkishairlines.technology.dt.route_wings.mapper.LocationMapper;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.repository.LocationRepository;
import com.turkishairlines.technology.dt.route_wings.service.LocationService;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final TransportationService transportationService;

    @Override
    public List<LocationResponseDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(LocationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LocationResponseDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id).orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, LocationConstants.ID, id));
        return LocationMapper.toResponseDTO(location);
    }

    @Override
    public LocationResponseDTO getLocationByName(String name) {
        Location location = locationRepository.findByName(name).orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, LocationConstants.NAME, name));
        return LocationMapper.toResponseDTO(location);
    }

    @Override
    public LocationResponseDTO saveLocation(LocationRequestDTO requestDTO) {
        // TODO: IATA Control will be added
        if (locationRepository.findByName(requestDTO.getName()).isPresent()) {
            throw new AlreadyExistsException(LocationConstants.LOCATION, LocationConstants.NAME, requestDTO.getName());
        }

        Location location = locationRepository.save(LocationMapper.toEntity(requestDTO));
        return LocationMapper.toResponseDTO(location);
    }

    @Override
    public LocationResponseDTO updateLocation(Long id, LocationRequestDTO requestDTO) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, LocationConstants.ID, id));

        if (requestDTO.getName() != null) {
            Optional<Location> existingLocation = locationRepository.findByName(requestDTO.getName());
            if (existingLocation.isPresent() && !existingLocation.get().getId().equals(id)) {
                throw new AlreadyExistsException(LocationConstants.LOCATION, LocationConstants.NAME, requestDTO.getName());
            }
            location.setName(requestDTO.getName());
        }
        if (requestDTO.getCity() != null && !requestDTO.getCity().isBlank()) location.setCity(requestDTO.getCity());
        if (requestDTO.getCountry() != null && !requestDTO.getCountry().isBlank())
            location.setCountry(requestDTO.getCountry());
        if (requestDTO.getLocationCode() != null && !requestDTO.getLocationCode().isBlank())
            location.setLocationCode(requestDTO.getLocationCode());

        return LocationMapper.toResponseDTO(locationRepository.save(location));
    }

    @Override
    public void deleteLocationById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NotFoundException(LocationConstants.LOCATION, LocationConstants.ID, id);
        }

        if (transportationService.isLocationUsed(getLocationEntityById(id))) {
            throw new IllegalStateException(ErrorMessages.LOCATION_IN_USE_MESSAGE);
        }

        locationRepository.deleteById(id);
    }

    @Override
    public Location getLocationEntityById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, LocationConstants.ID, id));
    }

    @Override
    public Location getByLocationCode(String locationCode) {
        return locationRepository.findByLocationCode(locationCode)
                .orElseThrow(() -> new NotFoundException(LocationConstants.LOCATION, LocationConstants.LOCATION_CODE, locationCode));
    }
}
