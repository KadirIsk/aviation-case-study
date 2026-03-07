package com.aviation.routing.flight.path.engine.application.service.impl;

import com.aviation.routing.flight.path.engine.application.dto.CreateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.dto.LocationFilterRequest;
import com.aviation.routing.flight.path.engine.application.dto.UpdateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.exception.DuplicateResourceException;
import com.aviation.routing.flight.path.engine.application.exception.ResourceNotFoundException;
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
    private final LocationPersistencePort persistencePort;
    private final TransportationPersistencePort transportationPersistencePort;

    @Transactional
    @Override
    public Location create(CreateLocationUseCase request) {
        boolean exists = persistencePort.existsByNameOrLocationCode(request.name(), request.locationCode());
        if (exists) {
            throw new DuplicateResourceException(ErrorCode.LOC_DUP_001, null);
        }

        Location location = Location.builder()
            .name(request.name())
            .country(request.country())
            .city(request.city())
            .locationCode(request.locationCode())
            .build();

        return persistencePort.save(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Location get(Long id) {
        return persistencePort.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TRN_NF_001, null));
    }

    @Override
    @Transactional
    public Location update(UpdateLocationUseCase request) {
        Location existing = Location.builder()
            .id(request.id())
            .name(request.name())
            .country(request.country())
            .city(request.city())
            .build();

        return persistencePort.update(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        persistencePort.deleteById(id);
        transportationPersistencePort.deleteByLocationId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Location> get(LocationFilterRequest filter, Pageable pageable) {
        return persistencePort.findAll(filter, pageable);
    }
}