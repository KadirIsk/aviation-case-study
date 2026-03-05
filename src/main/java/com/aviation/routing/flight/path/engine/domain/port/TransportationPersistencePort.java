package com.aviation.routing.flight.path.engine.domain.port;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TransportationPersistencePort {
    Transportation save(Transportation transportation);
    Transportation get(Long id);
    List<Transportation> getByOriginLocationId(Long originLocationId);
    void delete(Long id);
    Transportation update(Transportation transportation);
    Page<Transportation> getTransportations(TransportationRequest filter, Pageable pageable);
    Slice<Transportation> findAllByOrderByOriginLocationId(Pageable pageable);
}