package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import com.aviation.routing.flight.path.engine.domain.model.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Location response model")
public record LocationResponse(
    @Schema(description = "ID of the location", example = "1")
    Long id,
    @Schema(description = "Name of the location", example = "Taksim Meydani")
    String name,
    @Schema(description = "Country of the location", example = "Turkey")
    String country,
    @Schema(description = "City of the location", example = "Istanbul")
    String city,
    @Schema(description = "Location code", example = "IST")
    String locationCode
) {
    public static LocationResponse from(Location location) {
        if (location == null) {
            return null;
        }
        return LocationResponse.builder()
            .id(location.getId())
            .name(location.getName())
            .country(location.getCountry())
            .city(location.getCity())
            .locationCode(location.getLocationCode())
            .build();
    }
}
