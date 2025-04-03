package com.turkishairlines.technology.dt.route_wings.repository;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import com.turkishairlines.technology.dt.route_wings.model.transportation.Transportation;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    List<Transportation> findAllByOriginLocation(Location origin);

    boolean existsByOriginLocation(Location origin);

    boolean existsByDestinationLocation(Location destination);

    Optional<Transportation> findByOriginLocationAndDestinationLocationAndTransportationType(
            Location origin, Location destination, TransportationType transportationType);
}
