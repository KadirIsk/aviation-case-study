package com.aviation.routing.flight.path.engine.application.service.impl;

import com.aviation.routing.flight.path.engine.application.dto.CreateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.dto.LocationFilterRequest;
import com.aviation.routing.flight.path.engine.application.dto.UpdateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.exception.DuplicateResourceException;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.common.exception.ErrorCode;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.port.LocationPersistencePort;
import com.aviation.routing.flight.path.engine.domain.port.TransportationPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationPersistencePort locationPersistencePort;
    private final TransportationPersistencePort transportationPersistencePort;

    @Transactional
    @Override
    public Location createLocation(CreateLocationUseCase request) {
        boolean exists = locationPersistencePort.existsByNameOrLocationCode(request.name(), request.locationCode());
        if (exists) {
            throw new DuplicateResourceException(ErrorCode.LOC_DUP_001, null);
        }

        Location location = Location.builder()
            .name(request.name())
            .country(request.country())
            .city(request.city())
            .locationCode(request.locationCode())
            .build();

        return locationPersistencePort.save(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Location getLocation(Long id) {
        return locationPersistencePort.findById(id)
            .orElseThrow(() -> new DuplicateResourceException(ErrorCode.TRN_DUP_001, null));
    }

    @Override
    @Transactional
    public Location updateLocation(UpdateLocationUseCase request) {
        Location existing = getLocation(request.id());

        existing.setName(request.name());

        return locationPersistencePort.save(existing);
    }

    @Override
    @Transactional
    public void deleteLocation(Long id) {
        locationPersistencePort.deleteById(id);
        transportationPersistencePort.deleteByLocationId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Location> getLocations(LocationFilterRequest filter, Pageable pageable) {
        return locationPersistencePort.findAll(filter, pageable);
    }
}