package com.aviation.routing.flight.path.engine.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.aviation.routing.flight.path.engine.application.port.out.LocationProviderPort;
import com.aviation.routing.flight.path.engine.domain.model.projection.PathEdge;
import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RouteMapperServiceTest {

    @Mock
    private LocationProviderPort locationProviderPort;

    @InjectMocks
    private RouteMapperService routeMapperService;

    @Test
    void mapToResponses_whenEmpty_shouldReturnEmpty() {
        List<RouteResponse> responses = routeMapperService.mapToResponses(Collections.emptyList());
        assertTrue(responses.isEmpty());
    }

    @Test
    void mapToResponses_shouldMapCorrectlty() {
        PathEdge edge1 = PathEdge.builder()
            .originId(1L)
            .destinationId(2L)
            .transportationType("FLIGHT")
            .build();
        
        RouteCandidate candidate = RouteCandidate.builder()
            .edges(List.of(edge1))
            .build();

        when(locationProviderPort.getLocationNames(anySet())).thenReturn(Map.of(
            1L, "Istanbul",
            2L, "London"
        ));

        List<RouteResponse> result = routeMapperService.mapToResponses(List.of(candidate));

        assertEquals(1, result.size());
        RouteResponse response = result.get(0);
        assertEquals("via Istanbul", response.route().title());
        assertEquals(1, response.route().steps().size());
        assertEquals("Istanbul", response.route().steps().get(0).origin());
        assertEquals("London", response.route().steps().get(0).destination());
        assertEquals("FLIGHT", response.route().steps().get(0).transportationType());
    }

    @Test
    void mapToResponses_whenBus_shouldHaveUnknownRouteTitle() {
        PathEdge edge1 = PathEdge.builder()
            .originId(1L)
            .destinationId(2L)
            .transportationType("BUS")
            .build();
        
        RouteCandidate candidate = RouteCandidate.builder()
            .edges(List.of(edge1))
            .build();

        when(locationProviderPort.getLocationNames(anySet())).thenReturn(Map.of(
            1L, "Istanbul",
            2L, "Ankara"
        ));

        List<RouteResponse> result = routeMapperService.mapToResponses(List.of(candidate));

        assertEquals(1, result.size());
        assertEquals("Unknown Route", result.get(0).route().title());
    }
}
