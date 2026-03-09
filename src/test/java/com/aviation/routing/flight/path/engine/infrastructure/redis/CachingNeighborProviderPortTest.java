package com.aviation.routing.flight.path.engine.infrastructure.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
class CachingNeighborProviderPortTest {

    private Cache<Long, Map<String, EdgeInfo>> localCache;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private TransportationService transportationService;
    @Mock
    private RMap rMap;

    private CachingNeighborProviderPort neighborProviderPort;

    @BeforeEach
    void setUp() {
        localCache = Caffeine.newBuilder().build();
        neighborProviderPort = new CachingNeighborProviderPort(localCache, redissonClient, transportationService);
    }

    @Test
    void getNeighbors_shouldFetchFromRedisWhenCacheEmpty() {
        Long nodeId = 1L;
        short dayMask = 1;
        Map<String, String> redisData = new HashMap<>();
        redisData.put("2:FLIGHT", "FLIGHT:1");

        when(redissonClient.getMap("node:edges:" + nodeId)).thenReturn(rMap);
        when(rMap.entrySet()).thenReturn(redisData.entrySet());

        Map<String, EdgeInfo> result = neighborProviderPort.getNeighbors(nodeId, dayMask);

        assertEquals(1, result.size());
        assertTrue(result.containsKey("2:FLIGHT"));
        assertEquals("FLIGHT", result.get("2:FLIGHT").type());
        assertEquals((short) 1, result.get("2:FLIGHT").operatingDaysMask());
        
        // Second call should come from local cache
        neighborProviderPort.getNeighbors(nodeId, dayMask);
        verify(redissonClient, times(1)).getMap(anyString());
    }

    @Test
    void getNeighbors_shouldFilterByDayMask() {
        Long nodeId = 1L;
        // Requesting Tuesday (2)
        short requestedDayMask = 2; 
        
        Map<String, EdgeInfo> cachedData = new HashMap<>();
        // Edge 1: Monday only (1)
        cachedData.put("2:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));
        // Edge 2: Tuesday only (2)
        cachedData.put("3:BUS", new EdgeInfo("BUS", (short) 2));
        
        localCache.put(nodeId, cachedData);

        Map<String, EdgeInfo> result = neighborProviderPort.getNeighbors(nodeId, requestedDayMask);

        assertEquals(1, result.size());
        assertTrue(result.containsKey("3:BUS"));
        assertFalse(result.containsKey("2:FLIGHT"));
    }

    @Test
    void fetchFromRedis_shouldHandleEmptyData() {
        when(redissonClient.getMap(anyString())).thenReturn(rMap);
        when(rMap.isEmpty()).thenReturn(true);

        Map<String, EdgeInfo> result = neighborProviderPort.fetchFromRedis(1L);

        assertTrue(result.isEmpty());
    }
}
