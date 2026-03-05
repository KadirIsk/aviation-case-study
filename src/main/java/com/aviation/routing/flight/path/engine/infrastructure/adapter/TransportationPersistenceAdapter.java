package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.port.TransportationPersistencePort;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper.LocationMapper;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper.TransportationMapper;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.repository.JpaTransportationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.specification.TransportationSpecifications;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TransportationPersistenceAdapter implements TransportationPersistencePort {
    private final JpaTransportationRepository jpaRepository;
    private final RedissonClient redissonClient;

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

        Transportation saved = TransportationMapper.toDomain(entity);

        updateGraphAndBroadcast(saved);

        return saved;
    }

    private void updateGraphAndBroadcast(Transportation saved) {
        String value = saved.getTransportationType() + ":" + saved.getOperatingDays();
        redissonClient.getMap("node:edges:" + saved.getOriginLocationId())
                      .put(saved.getDestinationLocationId(), value);

        redissonClient.getTopic("graph-invalidation-topic").publish(saved.getOriginLocationId());
    }

    @Override
    public Transportation update(Transportation transportation) {
        // fixme: burada cok fazla mapping islemi yapiliyor, direkt update script'i calistirsak
        Transportation existing = get(transportation.getId());
        existing.setOperatingDays(transportation.getOperatingDays());

        TransportationEntity entity = jpaRepository.save(TransportationMapper.toEntity(existing));

        updateGraphAndBroadcast(TransportationMapper.toDomain(entity));

        return existing;
    }

    @Override
    public void delete(Long id) {
        Optional<TransportationEntity> entityOptional = jpaRepository.findById(id);
        entityOptional.ifPresent(this::deleteTransportationAndBroadcast);
    }

    private void deleteTransportationAndBroadcast(TransportationEntity entity) {
        Long originId = entity.getOriginLocationEntityId();
        Long destinationId = entity.getDestinationLocationEntityId();

        jpaRepository.deleteById(entity.getId());
        redissonClient.getMap("node:edges:" + originId).remove(destinationId);
        redissonClient.getTopic("graph-invalidation-topic").publish(originId);
    }

    @Override
    @Transactional(readOnly = true)
    public Transportation get(Long id) {
        TransportationEntity entity = jpaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));// todo: burada custom exception firlat
        return TransportationMapper.toDomain(entity);
    }

    @Override
    public List<Transportation> getByOriginLocationId(Long originLocationId) {
        List<TransportationEntity> transportationEntities = jpaRepository.findByOriginLocationEntityId(originLocationId);

        return transportationEntities.stream()
            .map(entity -> Transportation.builder()
                .id(entity.getId())
                .originLocationId(entity.getOriginLocationEntityId())
                .originLocation(LocationMapper.toDomain(entity.getOriginLocationEntity()))
                .destinationLocationId(entity.getDestinationLocationEntityId())
                .destinationLocation(LocationMapper.toDomain(entity.getDestinationLocationEntity()))
                .transportationType(entity.getTransportationType())
                .operatingDays(entity.getOperatingDays())
                .build())
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transportation> getTransportations(TransportationRequest filter, Pageable pageable) {
        var spec = TransportationSpecifications.withFilters(filter);

        Page<TransportationEntity> entityPage = jpaRepository.findAll(spec, pageable);

        return entityPage.map(entity -> Transportation.builder()
            .id(entity.getId())
            .originLocationId(entity.getOriginLocationEntityId())
            .originLocation(LocationMapper.toDomain(entity.getOriginLocationEntity()))
            .destinationLocationId(entity.getDestinationLocationEntityId())
            .destinationLocation(LocationMapper.toDomain(entity.getDestinationLocationEntity()))
            .transportationType(entity.getTransportationType())
            .operatingDays(entity.getOperatingDays())
            .build());
    }

    @Override
    public Slice<Transportation> findAllByOrderByOriginLocationId(Pageable pageable) {
        Slice<TransportationEntity> transportationEntities = jpaRepository.findAllByOrderByOriginLocationId(pageable);

        return transportationEntities.map(entity -> Transportation.builder()
            .id(entity.getId())
            .originLocationId(entity.getOriginLocationEntityId())
            .destinationLocationId(entity.getDestinationLocationEntityId())
            .transportationType(entity.getTransportationType())
            .operatingDays(entity.getOperatingDays())
            .build());
    }
}