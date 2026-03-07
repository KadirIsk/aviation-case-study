package com.aviation.routing.flight.path.engine.domain.model.route.finder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response containing a single route")
public record RouteResponse(
    @Schema(description = "The route found")
    Route route
) { }