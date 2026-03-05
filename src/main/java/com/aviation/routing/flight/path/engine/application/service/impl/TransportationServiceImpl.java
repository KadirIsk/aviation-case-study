package com.aviation.routing.flight.path.engine.application.service.impl;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepositoryPort;
import com.aviation.routing.flight.path.engine.domain.service.GraphStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepositoryPort transportationRepositoryPort;
    private final GraphStatePort graphStatePort;

    @Override
    public Transportation createTransportation(TransportationRequest request) {
        Transportation transportation = Transportation.builder()
            .originLocationId(request.originLocationId())
            .destinationLocationId(request.destinationLocationId())
            .transportationType(request.transportationType())
            .operatingDays(request.operatingDays())
            .build();

        Transportation saved = transportationRepositoryPort.save(transportation);

        graphStatePort.updateGraphAndBroadcast(saved);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Transportation getTransportation(Long id) {
        return transportationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found")); // todo: burada custom exception firlat
    }

    @Override
    public List<Transportation> getByOriginLocationId(Long originLocationId) {
        return transportationRepositoryPort.getByOriginLocationId(originLocationId);
    }

    @Override
    public void deleteTransportation(Long id) {
        Optional<Transportation> optionalTransportation = transportationRepositoryPort.findById(id);
        optionalTransportation.ifPresent(graphStatePort::deleteTransportationAndBroadcast);
    }

    @Override
    public Transportation updateTransportation(Long id, TransportationRequest request) {
        Transportation existing = getTransportation(id);
        existing.setOperatingDays(request.operatingDays());

        existing = transportationRepositoryPort.save(existing);

        graphStatePort.updateGraphAndBroadcast(existing);

        return existing;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transportation> getTransportations(TransportationRequest filter, Pageable pageable) {
        return transportationRepositoryPort.findAll(filter, pageable);
    }

    @Override
    public Page<Transportation> findByOperatingDay(Integer dayValue, Pageable pageable) {
        return transportationRepositoryPort.findByOperatingDay(dayValue, pageable);
    }

    @Override
    public Slice<Transportation> findAllByOrderByOriginLocationId(Pageable pageable) {
        return transportationRepositoryPort.findAllByOrderByOriginLocationId(pageable);
    }
}