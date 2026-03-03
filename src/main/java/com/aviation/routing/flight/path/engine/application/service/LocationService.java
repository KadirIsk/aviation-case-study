package com.aviation.routing.flight.path.engine.application.service;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Location;

public interface LocationService {
    Location createLocation(LocationRequest request);
    Location getLocation(Long id);
    void deleteLocation(Long id);
}