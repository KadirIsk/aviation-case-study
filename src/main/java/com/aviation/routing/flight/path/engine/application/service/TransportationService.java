package com.aviation.routing.flight.path.engine.application.service;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TransportationService {
    Transportation create(TransportationRequest request);
    Transportation get(Long id);
    List<Transportation> getByOriginLocationId(Long originLocationId);
    void delete(Long id);
    Transportation update(Long id, TransportationRequest request);
    Page<Transportation> getTransportations(TransportationRequest filter, Pageable pageable);
    Slice<Transportation> findAllByOrderByOriginLocationId(Pageable pageable);
}