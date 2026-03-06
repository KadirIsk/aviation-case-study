package com.aviation.routing.flight.path.engine.application.dto;

import lombok.Builder;

@Builder
public record CreateLocationUseCase(
    String name,
    String country,
    String city,
    String locationCode
) { }