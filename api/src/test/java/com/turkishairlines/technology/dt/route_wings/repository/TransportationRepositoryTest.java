package com.turkishairlines.technology.dt.route_wings.repository;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransportationRepositoryTest {

    @Mock
    private TransportationRepository transportationRepository;

    @Mock
    private Location originLocation;

    @Mock
    private Location destinationLocation;

    @Test
    void testFindAllByOriginLocation() {
        Transportation mockTransportation = Transportation.builder()
                .originLocation(originLocation)
                .destinationLocation(destinationLocation)
                .transportationType(TransportationType.BUS)
                .operatingDays(Set.of(1, 2))
                .build();

        when(transportationRepository.findAllByOriginLocation(originLocation))
                .thenReturn(List.of(mockTransportation));

        List<Transportation> results = transportationRepository.findAllByOriginLocation(originLocation);

        assertEquals(1, results.size());
        assertEquals(TransportationType.BUS, results.get(0).getTransportationType());
    }

    @Test
    void testExistsByOriginLocation() {
        when(transportationRepository.existsByOriginLocation(originLocation)).thenReturn(true);
        assertTrue(transportationRepository.existsByOriginLocation(originLocation));
    }

    @Test
    void testExistsByDestinationLocation() {
        when(transportationRepository.existsByDestinationLocation(destinationLocation)).thenReturn(true);
        assertTrue(transportationRepository.existsByDestinationLocation(destinationLocation));
    }

    @Test
    void testFindByOriginDestinationType() {
        Transportation transportation = Transportation.builder()
                .originLocation(originLocation)
                .destinationLocation(destinationLocation)
                .transportationType(TransportationType.FLIGHT)
                .operatingDays(Set.of(1, 2, 3))
                .build();

        when(transportationRepository.findByOriginLocationAndDestinationLocationAndTransportationType(
                originLocation, destinationLocation, TransportationType.FLIGHT))
                .thenReturn(Optional.of(transportation));

        Optional<Transportation> result = transportationRepository
                .findByOriginLocationAndDestinationLocationAndTransportationType(
                        originLocation, destinationLocation, TransportationType.FLIGHT);

        assertTrue(result.isPresent());
        assertEquals(TransportationType.FLIGHT, result.get().getTransportationType());
    }
}
