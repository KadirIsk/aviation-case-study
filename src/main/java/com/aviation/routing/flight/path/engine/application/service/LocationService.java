package com.aviation.routing.flight.path.engine.application.service;

import com.aviation.routing.flight.path.engine.application.dto.CreateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.dto.LocationFilterRequest;
import com.aviation.routing.flight.path.engine.application.dto.UpdateLocationUseCase;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {
    Location createLocation(CreateLocationUseCase request);
    Location getLocation(Long id);
    Location updateLocation(UpdateLocationUseCase request);
    void deleteLocation(Long id);
    Page<Location> getLocations(LocationFilterRequest filter, Pageable pageable);
}