package com.aviation.routing.flight.path.engine.application.service.impl;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.repository.LocationPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationPersistencePort locationPersistencePort;

    @Transactional
    @Override
    public Location createLocation(LocationRequest request) {
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
                .orElseThrow(() -> new RuntimeException("Location not found"));  // todo: burada custom exception firlat
    }

    @Override
    @Transactional
    public Location updateLocation(Long id, LocationRequest request) {
        Location existing = getLocation(id);

        existing.setName(request.name());
        existing.setCountry(request.country());
        existing.setCity(request.city());
        existing.setLocationCode(request.locationCode());

        return locationPersistencePort.save(existing);
    }

    @Override
    public void deleteLocation(Long id) {
        // todo: burada transportation's kayitlari da guncellenmeli
        locationPersistencePort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Location> getLocations(LocationRequest filter, Pageable pageable) {
        return locationPersistencePort.findAll(filter, pageable);
    }
}