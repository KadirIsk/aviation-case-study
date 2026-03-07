package com.aviation.routing.flight.path.engine.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Filter request for locations")
public record LocationFilterRequest(
    @Schema(description = "Name of the location", example = "Taksim")
    String name,
    @Schema(description = "Country of the location", example = "Turkey")
    String country,
    @Schema(description = "City of the location", example = "Istanbul")
    String city,
    @Schema(description = "Location code", example = "IST")
    String locationCode
) {
}
