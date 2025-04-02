package com.turkishairlines.technology.dt.route_wings.service;

import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;

import java.time.LocalDate;
import java.util.List;

public interface RouteService {
    List<RouteDTO> findValidRoutes(String originCode, String destinationCode, LocalDate date);
}
