package com.aviation.routing.flight.path.engine.application.service;

import com.aviation.routing.flight.path.engine.application.dto.CreateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.dto.LocationFilterRequest;
import com.aviation.routing.flight.path.engine.application.dto.UpdateLocationUseCase;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {
    Location create(CreateLocationUseCase request);
    Location get(Long id);
    Location update(UpdateLocationUseCase request);
    void delete(Long id);
    Page<Location> get(LocationFilterRequest filter, Pageable pageable);
}