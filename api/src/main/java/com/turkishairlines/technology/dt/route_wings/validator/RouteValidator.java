package com.turkishairlines.technology.dt.route_wings.validator;

import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RouteValidator {

    private final TransportationService transportationService;

    public boolean isValid(List<Transportation> route) {
        return containsExactlyOneFlight(route) && hasAtMostOneNonFlightBeforeAndAfter(route);
    }

    public boolean isAvailableOnDate(List<Transportation> route, LocalDate date) {
        return route.stream()
                .allMatch(t -> transportationService.isTransportationAvailableOnDate(t.getId(), date));
    }

    private boolean containsExactlyOneFlight(List<Transportation> route) {
        return route.stream()
                .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                .count() == 1;
    }

    private boolean hasAtMostOneNonFlightBeforeAndAfter(List<Transportation> route) {
        int before = 0, after = 0;
        boolean flightFound = false;

        for (Transportation t : route) {
            if (t.getTransportationType() == TransportationType.FLIGHT) {
                if (flightFound) return false;
                flightFound = true;
            } else {
                if (!flightFound) before++;
                else after++;
            }
        }
        return before <= 1 && after <= 1;
    }
}
