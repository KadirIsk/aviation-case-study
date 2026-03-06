package com.aviation.routing.flight.path.engine.domain.model.projection;

import lombok.Builder;

@Builder
public record PathEdge(
    Long originId,
    Long destinationId,
    String transportationType
) { }