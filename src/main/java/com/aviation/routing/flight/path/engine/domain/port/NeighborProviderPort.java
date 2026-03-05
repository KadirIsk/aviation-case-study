package com.aviation.routing.flight.path.engine.domain.port;

import java.util.Map;

import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;

public interface NeighborProviderPort {
    Map<Long, EdgeInfo> getNeighbors(Long nodeId, short requestedDayMask);
}