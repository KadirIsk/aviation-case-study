package com.aviation.routing.flight.path.engine.infrastructure.persistence;

import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTransportationRepository extends JpaRepository<TransportationEntity, Long> {
}