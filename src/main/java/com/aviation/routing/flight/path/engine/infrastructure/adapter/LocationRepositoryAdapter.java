package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.repository.LocationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.JpaLocationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper.LocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationRepositoryAdapter implements LocationRepository {
    private final JpaLocationRepository jpaRepository;

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
    public List<Location> findAll() {
        List<LocationEntity> entities = jpaRepository.findAll();
        return entities.stream().map(LocationMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}