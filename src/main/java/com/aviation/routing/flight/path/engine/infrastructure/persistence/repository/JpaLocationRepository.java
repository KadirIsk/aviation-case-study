package com.aviation.routing.flight.path.engine.infrastructure.persistence.repository;

import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLocationRepository extends JpaRepository<LocationEntity, Long>,
    JpaSpecificationExecutor<LocationEntity> {
}