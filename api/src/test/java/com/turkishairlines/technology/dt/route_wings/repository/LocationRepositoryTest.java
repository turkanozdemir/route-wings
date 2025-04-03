package com.turkishairlines.technology.dt.route_wings.repository;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationRepositoryTest {
    @Mock
    private LocationRepository locationRepository;

    @Test
    void testFindByName() {
        Location location = new Location(1L, "Airport A", "Istanbul", "Turkey", "IST");
        when(locationRepository.findByName("Airport A")).thenReturn(Optional.of(location));

        Optional<Location> result = locationRepository.findByName("Airport A");

        assertTrue(result.isPresent());
        assertEquals("IST", result.get().getLocationCode());
    }
}
