package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response containing a single route")
public record RouteResponse(
    @Schema(description = "The route found")
    RouteDto route
) {
    @Builder
    @Schema(description = "Route information")
    public record RouteDto(
        @Schema(description = "Title of the route", example = "IST-LHR")
        String title,
        @Schema(description = "Steps to follow for this route")
        List<RouteStepDto> steps
    ) {}

    @Builder
    @Schema(description = "A single step in a route")
    public record RouteStepDto(
        @Schema(description = "Origin location code", example = "IST")
        String origin,
        @Schema(description = "Destination location code", example = "LHR")
        String destination,
        @Schema(description = "Type of transportation used", example = "FLIGHT")
        String transportationType
    ) {}
}
