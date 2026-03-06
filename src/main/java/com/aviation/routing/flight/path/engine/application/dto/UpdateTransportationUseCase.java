package com.aviation.routing.flight.path.engine.application.dto;

import lombok.Builder;

@Builder
public record UpdateTransportationUseCase(
    Long id,
    Short operatingDays
) { }