package com.aviation.routing.flight.path.engine.domain.service;

import java.util.Map;

import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;

public interface NeighborProvider {
    Map<Long, EdgeInfo> getNeighbors(Long nodeId, short requestedDayMask);
}