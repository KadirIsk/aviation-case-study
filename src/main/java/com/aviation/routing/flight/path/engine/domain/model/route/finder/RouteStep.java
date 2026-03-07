package com.aviation.routing.flight.path.engine.domain.model.route.finder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "A single step in a route")
public record RouteStep(
    @Schema(description = "Origin location code", example = "IST")
    String origin,
    @Schema(description = "Destination location code", example = "LHR")
    String destination,
    @Schema(description = "Type of transportation used", example = "FLIGHT")
    String transportationType
) { }