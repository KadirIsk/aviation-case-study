package com.aviation.routing.flight.path.engine.domain.model.route.finder;

import lombok.Builder;

@Builder
public record RouteStep(
    String origin,
    String destination,
    String transportationType
) { }