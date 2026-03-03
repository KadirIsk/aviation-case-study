package com.aviation.routing.flight.path.engine.application.service.impl;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Transactional
    @Override
    public Location createLocation(LocationRequest request) {
        Location location = Location.builder()
                .name(request.name())
                .country(request.country())
                .city(request.city())
                .locationCode(request.locationCode())
                .build();

        return locationRepository.save(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Location getLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));  // todo: burada custom exception firlat
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}