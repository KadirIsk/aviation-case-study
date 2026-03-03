package com.aviation.routing.flight.path.engine.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TransportationRequest(
    @NotNull @Positive Long originLocationId,
    @NotNull @Positive Long destinationLocationId,
    @NotBlank String transportationType,
    @NotBlank String operatingDays
) { }