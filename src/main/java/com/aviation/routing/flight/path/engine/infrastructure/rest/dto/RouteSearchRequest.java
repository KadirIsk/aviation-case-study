package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import java.time.DayOfWeek;
import java.util.Set;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Request object for finding flight routes")
public record RouteSearchRequest(
    @Parameter(description = "ID of the origin location", example = "1")
    Long originId,
    @Parameter(description = "ID of the destination location", example = "2")
    Long destinationId,
    @Parameter(description = "Requested operating days")
    Set<DayOfWeek> operatingDays
) { }
