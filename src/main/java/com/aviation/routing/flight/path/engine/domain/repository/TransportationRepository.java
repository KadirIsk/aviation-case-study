package com.aviation.routing.flight.path.engine.domain.repository;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.domain.model.Transportation;

public interface TransportationRepository {
    Transportation save(Transportation location);
    Optional<Transportation> findById(Long id);
    List<Transportation> findAll();
    void deleteById(Long id);
}