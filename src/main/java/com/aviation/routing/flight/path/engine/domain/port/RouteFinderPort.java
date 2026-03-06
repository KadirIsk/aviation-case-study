package com.aviation.routing.flight.path.engine.domain.port;

public interface RouteFinderPort {
    void findRoute(Long originId, Long destinationId, short requestedDayMask);
}
