package com.aviation.routing.flight.path.engine.domain.port;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationPersistencePort {
    Location save(Location location);
    Optional<Location> findById(Long id);
    Page<Location> findAll(LocationRequest filter, Pageable pageable);
    void deleteById(Long id);
}