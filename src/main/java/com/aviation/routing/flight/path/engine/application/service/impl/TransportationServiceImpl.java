package com.aviation.routing.flight.path.engine.application.service.impl;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.port.TransportationPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationPersistencePort persistencePort;

    @Override
    public Transportation create(TransportationRequest request) {
        Transportation transportation = Transportation.builder()
            .originLocationId(request.originLocationId())
            .destinationLocationId(request.destinationLocationId())
            .transportationType(request.transportationType())
            .operatingDays(request.operatingDays())
            .build();

        return persistencePort.save(transportation);
    }

    @Override
    @Transactional(readOnly = true)
    public Transportation get(Long id) {
        return persistencePort.get(id);
    }

    @Override
    public List<Transportation> getByOriginLocationId(Long originLocationId) {
        return persistencePort.getByOriginLocationId(originLocationId);
    }

    @Override
    public void delete(Long id) {
        persistencePort.delete(id);
    }

    // todo: fonk input'u degistirelim
    @Override
    public Transportation update(Long id, TransportationRequest request) {
        Transportation transportation = Transportation.builder()
            .id(id)
            .originLocationId(request.originLocationId())
            .destinationLocationId(request.destinationLocationId())
            .transportationType(request.transportationType())
            .operatingDays(request.operatingDays())
            .build();
        return persistencePort.update(transportation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transportation> getTransportations(TransportationRequest filter, Pageable pageable) {
        return persistencePort.getTransportations(filter, pageable);
    }

    @Override
    public Slice<Transportation> findAllByOrderByOriginLocationId(Pageable pageable) {
        return persistencePort.findAllByOrderByOriginLocationId(pageable);
    }
}