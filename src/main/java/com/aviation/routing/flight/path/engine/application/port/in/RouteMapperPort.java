package com.aviation.routing.flight.path.engine.application.port.in;


import java.util.List;

import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.RouteResponse;

public interface RouteMapperPort {
    List<RouteResponse> mapToResponses(List<RouteCandidate> validRoutes);
}