package com.aviation.routing.flight.path.engine.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TransportationRequest(
    @Schema(description = "ID of the origin location", example = "1")
    @NotNull @Positive Long originLocationId,
    @Schema(description = "ID of the destination location", example = "2")
    @NotNull @Positive Long destinationLocationId,
    @Schema(description = "Type of the transportation", example = "FLIGHT, BUS, SUBWAY, UBER")
    @NotBlank String transportationType,
    @Schema(description = "Operating days of the transportation", example = "Monday, Tuesday")
    Short operatingDays
) { }