package com.aviation.routing.flight.path.engine.application.service;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TransportationService {
    Transportation createTransportation(TransportationRequest request);
    Transportation getTransportation(Long id);
    List<Transportation> getByOriginLocationId(Long originLocationId);
    void deleteTransportation(Long id);
    Transportation updateTransportation(Long id, TransportationRequest request);
    Page<Transportation> getTransportations(TransportationRequest filter, Pageable pageable);
    Page<Transportation> findByOperatingDay(Integer dayValue, Pageable pageable);
    Slice<Transportation> findAllByOrderByOriginLocationId(Pageable pageable);
}