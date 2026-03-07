package com.aviation.routing.flight.path.engine.application.port.in;

import java.util.List;

import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse;

public interface FindFlightRoutesUseCase {
    List<RouteResponse> execute(Long originId, Long destinationId, short requestedDayMask);
}