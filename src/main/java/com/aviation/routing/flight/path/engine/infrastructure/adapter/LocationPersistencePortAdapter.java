package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.LocationFilterRequest;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.port.LocationPersistencePort;
import com.aviation.routing.flight.path.engine.domain.port.TransportationPersistencePort;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper.LocationMapper;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.repository.JpaLocationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.specification.LocationSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationPersistencePortAdapter implements LocationPersistencePort {
    private final JpaLocationRepository jpaRepository;
    private final TransportationPersistencePort transportationPersistencePort;

    @Override
    public Location save(Location location) {
        LocationEntity entity = LocationMapper.toEntity(location);
        entity = jpaRepository.save(entity);
        return LocationMapper.toDomain(entity);
    }

    @Override
    public Optional<Location> findById(Long id) {
        Optional<LocationEntity> entityOpt = jpaRepository.findById(id);
        return entityOpt.map(LocationMapper::toDomain);
    }

    @Override
    public Page<Location> findAll(LocationFilterRequest filter, Pageable pageable) {
        var spec = LocationSpecifications.withFilters(filter);

        Page<LocationEntity> entityPage = jpaRepository.findAll(spec, pageable);

        return entityPage.map(entity -> Location.builder()
            .id(entity.getId())
            .name(entity.getName())
            .country(entity.getCountry())
            .city(entity.getCity())
            .locationCode(entity.getLocationCode())
            .build());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameOrLocationCode(String name, String locationCode) {
        return jpaRepository.existsByNameOrLocationCode(name, locationCode);
    }
}