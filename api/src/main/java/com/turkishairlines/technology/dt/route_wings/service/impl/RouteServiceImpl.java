package com.turkishairlines.technology.dt.route_wings.service.impl;

import com.turkishairlines.technology.dt.route_wings.mapper.TransportationMapper;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import com.turkishairlines.technology.dt.route_wings.repository.LocationRepository;
import com.turkishairlines.technology.dt.route_wings.repository.TransportationRepository;
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
    private final LocationRepository locationRepository;
    private final TransportationRepository transportationRepository;
    private final TransportationService transportationService; // Yeni eklenen dependency

    public List<RouteDTO> findValidRoutes(String originCode, String destinationCode, LocalDate date) {
        Location origin = locationRepository.findByLocationCode(originCode)
                .orElseThrow(() -> new IllegalArgumentException("Origin not found"));
        Location destination = locationRepository.findByLocationCode(destinationCode)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        List<List<Transportation>> allPossibleRoutes = new ArrayList<>();
        Set<Transportation> visitedTransportations = new HashSet<>();

        dfs(origin, destination, new ArrayList<>(), allPossibleRoutes, visitedTransportations, 0);

        return allPossibleRoutes.stream()
                .filter(this::isValidRoute)
                .filter(route -> date == null || isRouteAvailableOnDate(route, date))
                .map(route -> RouteDTO.builder()
                        .steps(route.stream()
                                .map(TransportationMapper::toResponseDTO)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private void dfs(Location currentLocation, Location destinationLocation, List<Transportation> currentPath,
                     List<List<Transportation>> foundRoutes, Set<Transportation> visitedTransportations, int depth) {
        if (depth >= 3) return;

        for (Transportation transportation : transportationRepository.findAllByOriginLocation(currentLocation)) {
            if (visitedTransportations.contains(transportation)) continue;
            visitedTransportations.add(transportation);
            currentPath.add(transportation);

            if (transportation.getDestinationLocation().equals(destinationLocation)) {
                foundRoutes.add(new ArrayList<>(currentPath));
            } else {
                dfs(transportation.getDestinationLocation(), destinationLocation, currentPath, foundRoutes, visitedTransportations, depth + 1);
            }

            currentPath.remove(currentPath.size() - 1);
            visitedTransportations.remove(transportation);
        }
    }

    private boolean isRouteAvailableOnDate(List<Transportation> route, LocalDate date) {
        return route.stream()
                .allMatch(transportation -> transportationService.isTransportationAvailableOnDate(transportation.getId(), date));
    }

    private boolean isValidRoute(List<Transportation> route) {
        long flightCount = route.stream().filter(transportation -> transportation.getTransportationType() == TransportationType.FLIGHT).count();
        if (flightCount != 1 || route.size() > 3) return false;

        List<Transportation> transportsBeforeFlight = new ArrayList<>();
        List<Transportation> transportsAfterFlight = new ArrayList<>();
        boolean flightFound = false;

        for (Transportation transportation : route) {
            if (transportation.getTransportationType() == TransportationType.FLIGHT) {
                if (flightFound) return false;
                flightFound = true;
            } else {
                if (!flightFound) transportsBeforeFlight.add(transportation);
                else transportsAfterFlight.add(transportation);
            }
        }

        return transportsBeforeFlight.size() <= 1 && transportsAfterFlight.size() <= 1;
    }
}
