package com.aviation.routing.flight.path.engine.application.service.impl;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepository transportationRepository;

    @Transactional
    @Override
    public Transportation createTransportation(TransportationRequest request) {
        Transportation transportation = Transportation.builder()
            .originLocationId(request.originLocationId())
            .destinationLocationId(request.destinationLocationId())
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

    @Override
    @Transactional
    public Transportation updateTransportation(Long id, TransportationRequest request) {
        Transportation existing = getTransportation(id);

        existing.setOriginLocationId(request.originLocationId());
        existing.setDestinationLocationId(request.destinationLocationId());
        existing.setTransportationType(request.transportationType());
        existing.setOperatingDays(request.operatingDays());

        return transportationRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transportation> getTransportations(TransportationRequest filter, Pageable pageable) {
        return transportationRepository.findAll(filter, pageable);
    }

    @Override
    public Page<Transportation> findByOperatingDay(Integer dayValue, Pageable pageable) {
        return transportationRepository.findByOperatingDay(dayValue, pageable);
    }
}