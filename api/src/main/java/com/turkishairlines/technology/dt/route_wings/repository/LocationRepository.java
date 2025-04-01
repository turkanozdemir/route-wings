package com.turkishairlines.technology.dt.route_wings.repository;

import com.turkishairlines.technology.dt.route_wings.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByName(String name);
}
