package com.turkishairlines.technology.dt.route_wings.mapper;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransportationMapperTest {

    @Test
    void toEntity_ShouldMapCorrectly() {
        Location origin = new Location();
        origin.setId(1L);
        origin.setName("Istanbul");

        Location destination = new Location();
        destination.setId(2L);
        destination.setName("Ankara");

        TransportationRequestDTO dto = TransportationRequestDTO.builder()
                .originId(1L)
                .destinationId(2L)
                .transportationType(TransportationType.FLIGHT)
                .operatingDays(Set.of(1, 2, 3))
                .build();

        Transportation transportation = TransportationMapper.toEntity(dto, origin, destination);

        assertEquals(origin, transportation.getOriginLocation());
        assertEquals(destination, transportation.getDestinationLocation());
        assertEquals(TransportationType.FLIGHT, transportation.getTransportationType());
        assertTrue(transportation.getOperatingDays().contains(2));
    }

    @Test
    void toResponseDTO_ShouldMapCorrectly() {
        Location origin = new Location();
        origin.setName("Istanbul");

        Location destination = new Location();
        destination.setName("Ankara");

        Transportation transportation = Transportation.builder()
                .id(10L)
                .originLocation(origin)
                .destinationLocation(destination)
                .transportationType(TransportationType.UBER)
                .operatingDays(Set.of(3, 4))
                .build();

        TransportationResponseDTO dto = TransportationMapper.toResponseDTO(transportation);

        assertEquals(10L, dto.getId());
        assertEquals("Istanbul", dto.getOriginName());
        assertEquals("Ankara", dto.getDestinationName());
    }
}
