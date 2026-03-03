package com.aviation.routing.flight.path.engine.domain.repository;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.domain.model.Location;

public interface LocationRepository {
    Location save(Location location);
    Optional<Location> findById(Long id);
    List<Location> findAll();
    void deleteById(Long id);
}