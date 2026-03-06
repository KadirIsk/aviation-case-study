package com.aviation.routing.flight.path.engine.domain.service.impl;

import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.port.RouteRulePort;

public class SingleFlightRule implements RouteRulePort {
    @Override
    public boolean isSatisfiedBy(RouteCandidate candidate) {
        long flightCount = candidate.edges().stream()
                .filter(edge -> "FLIGHT".equalsIgnoreCase(edge.transportationType()))
                .count();
        
        return flightCount == 1;
    }
}