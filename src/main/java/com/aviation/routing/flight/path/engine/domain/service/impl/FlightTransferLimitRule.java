package com.aviation.routing.flight.path.engine.domain.service.impl;

import java.util.List;

import com.aviation.routing.flight.path.engine.domain.model.projection.PathEdge;
import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.port.RouteRulePort;

public class FlightTransferLimitRule implements RouteRulePort {
    @Override
    public boolean isSatisfiedBy(RouteCandidate candidate) {
        List<PathEdge> edges = candidate.edges();
        int flightIndex = -1;

        for (int i = 0; i < edges.size(); i++) {
            if ("FLIGHT".equalsIgnoreCase(edges.get(i).transportationType())) {
                flightIndex = i;
                break;
            }
        }

        if (flightIndex == -1) {
            return false;
        }

        int transfersBefore = flightIndex;
        int transfersAfter = edges.size() - 1 - flightIndex;

        return transfersBefore <= 1 && transfersAfter <= 1;
    }
}