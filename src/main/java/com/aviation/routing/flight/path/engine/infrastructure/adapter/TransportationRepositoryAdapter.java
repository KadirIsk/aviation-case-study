package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.JpaTransportationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper.TransportationMapper;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.specification.TransportationSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Transportation> findAll(TransportationRequest filter, Pageable pageable) {
        var spec = TransportationSpecifications.withFilters(filter);

        Page<TransportationEntity> entityPage = jpaRepository.findAll(spec, pageable);

        return entityPage.map(entity -> Transportation.builder()
            .id(entity.getId())
            .originLocationId(entity.getOriginLocationEntityId())
            .destinationLocationId(entity.getDestinationLocationEntityId())
            .transportationType(entity.getTransportationType())
            .operatingDays(entity.getOperatingDays())
            .build());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}