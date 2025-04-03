package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.exception.AlreadyExistsException;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.repository.LocationRepository;
import com.turkishairlines.technology.dt.route_wings.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {

    @InjectMocks
    private LocationServiceImpl locationService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private TransportationService transportationService;

    @Test
    void testGetAllLocations() {
        List<Location> locations = List.of(
                new Location(1L, "Loc1", "City1", "Country1", "LOC1"),
                new Location(2L, "Loc2", "City2", "Country2", "LOC2")
        );
        when(locationRepository.findAll()).thenReturn(locations);

        List<LocationResponseDTO> result = locationService.getAllLocations();

        assertEquals(2, result.size());
        verify(locationRepository).findAll();
    }

    @Test
    void testSaveLocationThrowsAlreadyExists() {
        LocationRequestDTO request = new LocationRequestDTO("Loc1", "City", "Country", "CODE1");
        when(locationRepository.findByName("Loc1")).thenReturn(Optional.of(new Location()));

        assertThrows(AlreadyExistsException.class, () -> locationService.saveLocation(request));
    }

    @Test
    void testDeleteLocationInUse() {
        Location location = new Location(1L, "Loc", "City", "Country", "LOC");
        when(locationRepository.existsById(1L)).thenReturn(true);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(transportationService.isLocationUsed(location)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> locationService.deleteLocationById(1L));
    }
}
