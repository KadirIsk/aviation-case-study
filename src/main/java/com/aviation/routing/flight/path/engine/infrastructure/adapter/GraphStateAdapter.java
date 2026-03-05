package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepositoryPort;
import com.aviation.routing.flight.path.engine.domain.service.GraphStatePort;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GraphStateAdapter implements GraphStatePort {
    private final TransportationRepositoryPort repository;
    private final RedissonClient redissonClient;

    @Override
    public void updateGraphAndBroadcast(Transportation transportation) {
        Long originId = transportation.getOriginLocationId();
        Long destinationId = transportation.getDestinationLocationId();
        String value = transportation.getTransportationType() + ":" + transportation.getOperatingDays();

        redissonClient.getMap("node:edges:" + originId).put(destinationId, value);

        redissonClient.getTopic("graph-invalidation-topic").publish(originId);
    }

    @Override
    @Transactional
    public void deleteTransportationAndBroadcast(Transportation transportation) {
        Long originId = transportation.getOriginLocationId();
        Long destinationId = transportation.getDestinationLocationId();

        repository.deleteById(transportation.getId());

        redissonClient.getMap("node:edges:" + originId).remove(destinationId);

        redissonClient.getTopic("graph-invalidation-topic").publish(originId);
    }
}