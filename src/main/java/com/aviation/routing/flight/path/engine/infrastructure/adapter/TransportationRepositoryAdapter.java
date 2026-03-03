package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.JpaTransportationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper.TransportationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransportationRepositoryAdapter implements TransportationRepository {
    private final JpaTransportationRepository jpaRepository;

    @Override
    public Transportation save(Transportation transportation) {
        TransportationEntity entity = TransportationEntity.builder()
            .id(transportation.getId())
            .originLocationEntityId(transportation.getOriginLocationId())
            .destinationLocationEntityId(transportation.getDestinationLocationId())
            .transportationType(transportation.getTransportationType())
            .operatingDays(transportation.getOperatingDays())
            .build();

        entity = jpaRepository.save(entity);

        return TransportationMapper.toDomain(entity);
    }

    @Override
    public Optional<Transportation> findById(Long id) {
        Optional<TransportationEntity> entityOptional = jpaRepository.findById(id);
        return entityOptional.map(TransportationMapper::toDomain);
    }

    @Override
    public List<Transportation> findAll() {
        List<TransportationEntity> entities = jpaRepository.findAll();
        return entities.stream().map(TransportationMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}