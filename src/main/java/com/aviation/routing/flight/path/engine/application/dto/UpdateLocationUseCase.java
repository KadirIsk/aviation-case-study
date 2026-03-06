package com.aviation.routing.flight.path.engine.application.dto;

import lombok.Builder;

@Builder
public record UpdateLocationUseCase(
    Long id,
    String name
) { }