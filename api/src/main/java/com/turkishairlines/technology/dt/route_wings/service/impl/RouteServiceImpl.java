package com.turkishairlines.technology.dt.route_wings.service.impl;

import com.turkishairlines.technology.dt.route_wings.mapper.RouteMapper;
import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.service.LocationService;
import com.turkishairlines.technology.dt.route_wings.service.RouteService;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import com.turkishairlines.technology.dt.route_wings.validator.RouteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private static final int MAX_ROUTE_DEPTH = 3;

    private final LocationService locationService;
    private final TransportationService transportationService;
    private final RouteValidator routeValidator;
    private final RouteMapper routeMapper;

    @Override
    public List<RouteDTO> findValidRoutes(String originCode, String destinationCode, LocalDate date) {
        Location origin = locationService.getByLocationCode(originCode);
        Location destination = locationService.getByLocationCode(destinationCode);

        List<List<Transportation>> allPossibleRoutes = new ArrayList<>();
        Set<Transportation> visited = new HashSet<>();

        dfs(origin, destination, new ArrayList<>(), allPossibleRoutes, visited, 0);

        Predicate<List<Transportation>> isValid = routeValidator::isValid;
        Predicate<List<Transportation>> isAvailable = route -> date == null || routeValidator.isAvailableOnDate(route, date);

        return allPossibleRoutes.stream()
                .filter(isValid.and(isAvailable))
                .map(routeMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void dfs(Location currentLocation, Location destinationLocation, List<Transportation> currentPath,
                     List<List<Transportation>> foundRoutes, Set<Transportation> visitedTransportations, int currentDepth) {

        if (currentDepth >= MAX_ROUTE_DEPTH) return;

        for (Transportation t : transportationService.getAllByOriginLocation(currentLocation)) {
            if (!visitedTransportations.add(t)) continue;

            currentPath.add(t);

            if (t.getDestinationLocation().equals(destinationLocation)) {
                foundRoutes.add(new ArrayList<>(currentPath));
            } else {
                dfs(t.getDestinationLocation(), destinationLocation, currentPath, foundRoutes, visitedTransportations, currentDepth + 1);
            }

            currentPath.remove(currentPath.size() - 1);
            visitedTransportations.remove(t);
        }
    }
}
