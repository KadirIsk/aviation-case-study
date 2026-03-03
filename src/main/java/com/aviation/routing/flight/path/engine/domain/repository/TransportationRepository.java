package com.aviation.routing.flight.path.engine.domain.repository;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransportationRepository {
    Transportation save(Transportation location);
    Optional<Transportation> findById(Long id);
    Page<Transportation> findAll(TransportationRequest filter, Pageable pageable);
    void deleteById(Long id);
}