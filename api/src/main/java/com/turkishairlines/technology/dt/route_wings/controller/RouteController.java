package com.turkishairlines.technology.dt.route_wings.controller;

import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<RouteDTO>> getValidRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<RouteDTO> routes = routeService.findValidRoutes(origin, destination, date);
        return ResponseEntity.ok(routes);
    }
}
