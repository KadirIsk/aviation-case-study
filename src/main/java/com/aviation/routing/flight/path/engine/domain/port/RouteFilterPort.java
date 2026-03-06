package com.aviation.routing.flight.path.engine.domain.port;

import java.util.List;

import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;

public interface RouteFilterPort {
    List<RouteCandidate> filterValidRoutes(List<RouteCandidate> candidates);
}