package com.turkishairlines.technology.dt.route_wings.mapper;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RouteMapperTest {

    private RouteMapper routeMapper;

    @BeforeEach
    void setUp() {
        routeMapper = new RouteMapper();
    }

    @Test
    void shouldMapTransportationListToRouteDTO() {
        Location origin = Location.builder().id(1L).name("IST").locationCode("IST").build();
        Location destination = Location.builder().id(2L).name("SAW").locationCode("SAW").build();

        Transportation transportation = Transportation.builder()
                .id(10L)
                .originLocation(origin)
                .destinationLocation(destination)
                .transportationType(TransportationType.FLIGHT)
                .operatingDays(Set.of(1, 2, 3))
                .build();

        RouteDTO dto = routeMapper.toDTO(List.of(transportation));

        assertNotNull(dto);
        assertEquals(1, dto.getSteps().size());
        TransportationResponseDTO step = dto.getSteps().get(0);
        assertEquals("IST", step.getOriginName());
        assertEquals("SAW", step.getDestinationName());
        assertEquals(TransportationType.FLIGHT, step.getTransportationType());
    }
}
