package com.aviation.routing.flight.path.engine.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TransportationRequest(
    @NotBlank Long originLocationId,
    @NotBlank Long destinationLocationId,
    @NotBlank String transportationType,
    @NotBlank String operatingDays
) { }