package com.aviation.routing.flight.path.engine.application.dto;

import java.time.DayOfWeek;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Filter request for transportations")
public record TransportationFilterRequest(
    @Schema(description = "ID of the origin location", example = "1")
    Long originLocationId,
    @Schema(description = "ID of the destination location", example = "2")
    Long destinationLocationId,
    @Schema(description = "Type of the transportation", example = "FLIGHT")
    String transportationType,
    @Schema(description = "Operating days of the transportation", example = "[\"MONDAY\", \"TUESDAY\"]")
    Set<DayOfWeek> operatingDays
) { }