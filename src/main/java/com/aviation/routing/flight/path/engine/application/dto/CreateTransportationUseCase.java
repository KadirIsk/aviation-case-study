package com.aviation.routing.flight.path.engine.application.dto;

import lombok.Builder;

@Builder
public record CreateTransportationUseCase(
    Long originLocationId,
    Long destinationLocationId,
    String transportationType,
    Short operatingDays
) { }