package com.aviation.routing.flight.path.engine.application.service.impl;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.port.in.FindFlightRoutesUseCase;
import com.aviation.routing.flight.path.engine.application.port.in.RouteMapperPort;
import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.port.RouteFilterPort;
import com.aviation.routing.flight.path.engine.domain.port.RouteFinderPort;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightRouteOrchestratorService implements FindFlightRoutesUseCase {
    private final RouteFinderPort routeFinderPort;
    private final RouteFilterPort routeFilterPort;
    private final RouteMapperPort routeMapperPort;

    @Override
    public List<RouteResponse> execute(Long originId, Long destinationId, short requestedDayMask) {
        List<RouteCandidate> allCandidates = routeFinderPort.findRoutes(originId, destinationId, requestedDayMask);

        List<RouteCandidate> validCandidates = routeFilterPort.filterValidRoutes(allCandidates);

        return routeMapperPort.mapToResponses(validCandidates);
    }
}