package com.aviation.routing.flight.path.engine.domain.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;
import com.aviation.routing.flight.path.engine.domain.port.NeighborProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RouteFinderServiceTest {

    @Mock
    private NeighborProviderPort neighborProviderPort;

    private RouteFinderService routeFinderService;

    @BeforeEach
    void setUp() {
        routeFinderService = new RouteFinderService(neighborProviderPort);
    }

    @Test
    void findRoutes_whenNoNeighbors_shouldReturnEmptyList() {
        when(neighborProviderPort.getNeighbors(1L, (short) 1)).thenReturn(Collections.emptyMap());

        List<RouteCandidate> result = routeFinderService.findRoutes(1L, 2L, (short) 1);

        assertTrue(result.isEmpty());
        verify(neighborProviderPort).getNeighbors(1L, (short) 1);
    }

    @Test
    void findRoutes_whenDirectRouteExists_shouldReturnIt() {
        Map<String, EdgeInfo> neighbors = new HashMap<>();
        neighbors.put("2:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));
        
        when(neighborProviderPort.getNeighbors(1L, (short) 1)).thenReturn(neighbors);

        List<RouteCandidate> result = routeFinderService.findRoutes(1L, 2L, (short) 1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).edges().size());
        assertEquals(1L, result.get(0).edges().get(0).originId());
        assertEquals(2L, result.get(0).edges().get(0).destinationId());
        assertEquals("FLIGHT", result.get(0).edges().get(0).transportationType());
    }

    @Test
    void findRoutes_whenMultiStepRouteExists_shouldReturnIt() {
        // 1 -> 2 (FLIGHT)
        Map<String, EdgeInfo> neighbors1 = new HashMap<>();
        neighbors1.put("2:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));
        when(neighborProviderPort.getNeighbors(1L, (short) 1)).thenReturn(neighbors1);

        // 2 -> 3 (BUS)
        Map<String, EdgeInfo> neighbors2 = new HashMap<>();
        neighbors2.put("3:BUS", new EdgeInfo("BUS", (short) 1));
        when(neighborProviderPort.getNeighbors(2L, (short) 1)).thenReturn(neighbors2);

        List<RouteCandidate> result = routeFinderService.findRoutes(1L, 3L, (short) 1);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).edges().size());
        assertEquals(1L, result.get(0).edges().get(0).originId());
        assertEquals(2L, result.get(0).edges().get(0).destinationId());
        assertEquals(2L, result.get(0).edges().get(1).originId());
        assertEquals(3L, result.get(0).edges().get(1).destinationId());
    }

    @Test
    void findRoutes_shouldNotExceedMaxEdges() {
        // 1 -> 2 -> 3 -> 4 -> 5
        Map<String, EdgeInfo> n1 = Map.of("2:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));
        Map<String, EdgeInfo> n2 = Map.of("3:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));
        Map<String, EdgeInfo> n3 = Map.of("4:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));
        Map<String, EdgeInfo> n4 = Map.of("5:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));

        when(neighborProviderPort.getNeighbors(1L, (short) 1)).thenReturn(n1);
        when(neighborProviderPort.getNeighbors(2L, (short) 1)).thenReturn(n2);
        when(neighborProviderPort.getNeighbors(3L, (short) 1)).thenReturn(n3);
        // n4 should not be called if MAX_EDGES is 3

        List<RouteCandidate> result = routeFinderService.findRoutes(1L, 5L, (short) 1);

        assertTrue(result.isEmpty(), "Should not find route with 4 edges when MAX_EDGES is 3");
    }

    @Test
    void findRoutes_shouldAvoidCycles() {
        // 1 -> 2 -> 1
        Map<String, EdgeInfo> n1 = Map.of("2:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));
        Map<String, EdgeInfo> n2 = Map.of("1:FLIGHT", new EdgeInfo("FLIGHT", (short) 1));

        when(neighborProviderPort.getNeighbors(1L, (short) 1)).thenReturn(n1);
        when(neighborProviderPort.getNeighbors(2L, (short) 1)).thenReturn(n2);

        List<RouteCandidate> result = routeFinderService.findRoutes(1L, 3L, (short) 1);

        assertTrue(result.isEmpty());
    }
}
