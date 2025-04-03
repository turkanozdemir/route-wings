package com.turkishairlines.technology.dt.route_wings.service.impl;

import com.turkishairlines.technology.dt.route_wings.mapper.TransportationMapper;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import com.turkishairlines.technology.dt.route_wings.service.LocationService;
import com.turkishairlines.technology.dt.route_wings.service.RouteService;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private static final int MAX_ROUTE_DEPTH = 3;

    private final LocationService locationService;
    private final TransportationService transportationService;

    @Override
    public List<RouteDTO> findValidRoutes(String originCode, String destinationCode, LocalDate date) {
        Location origin = locationService.getByLocationCode(originCode);
        Location destination = locationService.getByLocationCode(destinationCode);

        List<List<Transportation>> allPossibleRoutes = new ArrayList<>();
        Set<Transportation> visitedTransportations = new HashSet<>();

        dfs(origin, destination, new ArrayList<>(), allPossibleRoutes, visitedTransportations, 0);

        return allPossibleRoutes.stream()
                .filter(this::isValidRoute)
                .filter(route -> date == null || isRouteAvailableOnDate(route, date))
                .map(this::convertToRouteDTO)
                .collect(Collectors.toList());
    }

    private void dfs(Location currentLocation, Location destinationLocation, List<Transportation> currentPath,
                     List<List<Transportation>> foundRoutes, Set<Transportation> visitedTransportations, int depth) {

        if (depth >= MAX_ROUTE_DEPTH) return;

        for (Transportation transportation : transportationService.getAllByOriginLocation(currentLocation)) {
            if (visitedTransportations.contains(transportation)) continue;

            visitedTransportations.add(transportation);
            currentPath.add(transportation);

            if (transportation.getDestinationLocation().equals(destinationLocation)) {
                foundRoutes.add(new ArrayList<>(currentPath));
            } else {
                dfs(transportation.getDestinationLocation(), destinationLocation,
                        currentPath, foundRoutes, visitedTransportations, depth + 1);
            }

            currentPath.remove(currentPath.size() - 1);
            visitedTransportations.remove(transportation);
        }
    }

    private boolean isRouteAvailableOnDate(List<Transportation> route, LocalDate date) {
        return route.stream()
                .allMatch(transportation -> transportationService
                        .isTransportationAvailableOnDate(transportation.getId(), date));
    }

    private RouteDTO convertToRouteDTO(List<Transportation> route) {
        return RouteDTO.builder()
                .steps(route.stream()
                        .map(TransportationMapper::toResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private boolean isValidRoute(List<Transportation> route) {
        return containsExactlyOneFlight(route) && hasAtMostOneNonFlightBeforeAndAfter(route);
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
