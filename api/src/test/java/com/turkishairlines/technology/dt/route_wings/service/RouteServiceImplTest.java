package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.mapper.RouteMapper;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import com.turkishairlines.technology.dt.route_wings.service.impl.RouteServiceImpl;
import com.turkishairlines.technology.dt.route_wings.validator.RouteValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RouteServiceImplTest {

    @Mock
    private LocationService locationService;

    @Mock
    private TransportationService transportationService;

    @Mock
    private RouteValidator routeValidator;

    @Mock
    private RouteMapper routeMapper;

    @InjectMocks
    private RouteServiceImpl routeService;

    private Location origin;
    private Location destination;
    private Transportation flight;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        origin = Location.builder().id(1L).name("IST").locationCode("IST").build();
        destination = Location.builder().id(2L).name("SAW").locationCode("SAW").build();
        flight = Transportation.builder().id(10L).originLocation(origin).destinationLocation(destination).transportationType(TransportationType.FLIGHT).operatingDays(Set.of(1, 2, 3, 4, 5)).build();
    }

    @Test
    void shouldReturnValidRoutes() {
        when(locationService.getByLocationCode("IST")).thenReturn(origin);
        when(locationService.getByLocationCode("SAW")).thenReturn(destination);
        when(transportationService.getAllByOriginLocation(origin)).thenReturn(List.of(flight));
        when(routeValidator.isValid(anyList())).thenReturn(true);
        when(routeValidator.isAvailableOnDate(anyList(), any())).thenReturn(true);
        when(routeMapper.toDTO(anyList())).thenReturn(RouteDTO.builder().steps(List.of()).build());

        List<RouteDTO> routes = routeService.findValidRoutes("IST", "SAW", LocalDate.now());

        assertEquals(1, routes.size());
        verify(routeValidator).isValid(anyList());
        verify(routeValidator).isAvailableOnDate(anyList(), any());
        verify(routeMapper).toDTO(anyList());
    }

    @Test
    void shouldReturnEmptyListWhenNoValidRoutes() {
        when(locationService.getByLocationCode("IST")).thenReturn(origin);
        when(locationService.getByLocationCode("SAW")).thenReturn(destination);
        when(transportationService.getAllByOriginLocation(origin)).thenReturn(List.of(flight));
        when(routeValidator.isValid(anyList())).thenReturn(false);

        List<RouteDTO> routes = routeService.findValidRoutes("IST", "SAW", LocalDate.now());

        assertTrue(routes.isEmpty());
        verify(routeValidator).isValid(anyList());
    }
}
