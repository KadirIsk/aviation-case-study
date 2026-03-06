package com.aviation.routing.flight.path.engine.infrastructure.redis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;
import com.aviation.routing.flight.path.engine.domain.port.NeighborProviderPort;
import com.github.benmanes.caffeine.cache.Cache;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CachingNeighborProviderPort implements NeighborProviderPort {
    private final Cache<Long, Map<Long, EdgeInfo>> localCache;
    private final RedissonClient redissonClient;
    private final TransportationService transportationService;

    @Override
    public Map<Long, EdgeInfo> getNeighbors(Long nodeId, short requestedDayMask) {
        Map<Long, EdgeInfo> allNeighbors = localCache.get(nodeId, this::fetchFromRedis);

        if (MapUtils.isEmpty(allNeighbors)) {
            return Collections.emptyMap();
        }

        return allNeighbors.entrySet().stream()
            .filter(entry -> (entry.getValue().operatingDaysMask() & requestedDayMask) > 0)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @CircuitBreaker(name = "redisNeighborCache", fallbackMethod = "fetchFromDbFallback")
    @TimeLimiter(name = "redisNeighborCache")
    public Map<Long, EdgeInfo> fetchFromRedis(Long nodeId) {
        RMap<Long, String> redisEdges = redissonClient.getMap("node:edges:" + nodeId);
        return convertToEdgeInfoMap(redisEdges);
    }

    private Map<Long, EdgeInfo> fetchFromDbFallback(Long nodeId, Throwable t) {
        log.error("Error fetching neighbors from DB for node [{}]: {}", nodeId, t.getMessage(), t);
        return fetchFromDb(nodeId);
    }

    private Map<Long, EdgeInfo> fetchFromDb(Long nodeId) {
        List<Transportation> edges = transportationService.getByOriginLocationId(nodeId);

        return edges.stream()
            .collect(Collectors.toMap(
                Transportation::getDestinationLocationId,
                t -> new EdgeInfo(t.getTransportationType(), t.getOperatingDays())
            ));
    }

    private Map<Long, EdgeInfo> convertToEdgeInfoMap(Map<Long, String> redisData) {
        if (MapUtils.isEmpty(redisData)) {
            return Collections.emptyMap();
        }

        return redisData.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> {
                    String[] parts = e.getValue().split(":");
                    return new EdgeInfo(parts[0], Short.parseShort(parts[1]));
                }
            ));
    }
}