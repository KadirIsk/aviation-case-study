package com.aviation.routing.flight.path.engine.domain.port;

import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;

public interface RouteRulePort {
    boolean isSatisfiedBy(RouteCandidate candidate);
}