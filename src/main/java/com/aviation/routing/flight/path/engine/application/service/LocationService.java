package com.aviation.routing.flight.path.engine.application.service;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {
    Location createLocation(LocationRequest request);
    Location getLocation(Long id);
    Location updateLocation(Long id, LocationRequest request);
    void deleteLocation(Long id);
    Page<Location> getLocations(LocationRequest filter, Pageable pageable);
}