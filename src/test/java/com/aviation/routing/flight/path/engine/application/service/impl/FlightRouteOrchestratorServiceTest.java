package com.aviation.routing.flight.path.engine.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.port.in.RouteMapperPort;
import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.port.RouteFilterPort;
import com.aviation.routing.flight.path.engine.domain.port.RouteFinderPort;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightRouteOrchestratorServiceTest {

    @Mock
    private RouteFinderPort routeFinderPort;
    @Mock
    private RouteFilterPort routeFilterPort;
    @Mock
    private RouteMapperPort routeMapperPort;

    @InjectMocks
    private FlightRouteOrchestratorService orchestratorService;

    @Test
    void execute_shouldOrchestrateFlow() {
        Long originId = 1L;
        Long destId = 2L;
        short dayMask = 1;

        RouteCandidate candidate = RouteCandidate.builder().edges(List.of()).build();
        List<RouteCandidate> candidates = List.of(candidate);
        RouteResponse response = RouteResponse.builder().build();
        List<RouteResponse> responses = List.of(response);

        when(routeFinderPort.findRoutes(originId, destId, dayMask)).thenReturn(candidates);
        when(routeFilterPort.filterValidRoutes(candidates)).thenReturn(candidates);
        when(routeMapperPort.mapToResponses(candidates)).thenReturn(responses);

        List<RouteResponse> result = orchestratorService.execute(originId, destId, dayMask);

        assertEquals(responses, result);
        verify(routeFinderPort).findRoutes(originId, destId, dayMask);
        verify(routeFilterPort).filterValidRoutes(candidates);
        verify(routeMapperPort).mapToResponses(candidates);
    }
}
