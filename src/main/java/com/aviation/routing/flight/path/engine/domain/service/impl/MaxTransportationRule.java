package com.aviation.routing.flight.path.engine.domain.service.impl;

import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.port.RouteRulePort;

public class MaxTransportationRule implements RouteRulePort {
    @Override
    public boolean isSatisfiedBy(RouteCandidate candidate) {
        return candidate.edges().size() <= 3;
    }
}