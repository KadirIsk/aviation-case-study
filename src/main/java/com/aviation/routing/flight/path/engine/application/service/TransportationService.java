package com.aviation.routing.flight.path.engine.application.service;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;

public interface TransportationService {
    Transportation createTransportation(TransportationRequest request);
    Transportation getTransportation(Long id);
    void deleteTransportation(Long id);
}