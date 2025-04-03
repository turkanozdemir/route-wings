package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.repository.TransportationRepository;
import com.turkishairlines.technology.dt.route_wings.service.impl.TransportationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransportationServiceImplTest {

    @Mock
    private TransportationRepository transportationRepository;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private TransportationServiceImpl transportationService;

    @Test
    void isTransportationAvailableOnDate_ShouldReturnTrue_WhenAvailable() {
        Transportation transportation = Transportation.builder()
                .id(1L)
                .operatingDays(Set.of(2, 4, 6))
                .build();

        when(transportationRepository.findById(1L)).thenReturn(Optional.of(transportation));

        boolean result = transportationService.isTransportationAvailableOnDate(1L, LocalDate.of(2025, 4, 1)); // Tuesday = 2

        assertTrue(result);
    }
}
