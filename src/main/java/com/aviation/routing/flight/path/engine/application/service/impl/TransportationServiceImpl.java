package com.aviation.routing.flight.path.engine.application.service.impl;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepository transportationRepository;
    private final LocationService locationService;

    @Transactional
    @Override
    public Transportation createTransportation(TransportationRequest request) {
        Location origin = locationService.getLocation(request.originLocationId());
        Location destination = locationService.getLocation(request.destinationLocationId());

        Transportation transportation = Transportation.builder()
            .originLocationId(origin.getId())
            .destinationLocationId(destination.getId())
            .transportationType(request.transportationType())
            .operatingDays(request.operatingDays())
            .build();

        return transportationRepository.save(transportation);
    }

    @Override
    @Transactional(readOnly = true)
    public Transportation getTransportation(Long id) {
        return transportationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found")); // todo: burada custom exception firlat
    }

    @Override
    public void deleteTransportation(Long id) {
        transportationRepository.deleteById(id);
    }
}