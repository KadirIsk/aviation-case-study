package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import java.time.DayOfWeek;
import java.util.Set;

import com.aviation.routing.flight.path.engine.common.util.DayOfWeekBitmaskMapper;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Transportation response model")
public record TransportationResponse(
    @Schema(description = "ID of the transportation", example = "1")
    Long id,
    @Schema(description = "Origin location details")
    LocationResponse originLocation,
    @Schema(description = "Destination location details")
    LocationResponse destinationLocation,
    @Schema(description = "Type of the transportation", example = "FLIGHT")
    String transportationType,
    @Schema(description = "Operating days of the transportation", example = "[\"MONDAY\", \"TUESDAY\"]")
    Set<DayOfWeek> operatingDays
) {
    public static TransportationResponse from(Transportation transportation) {
        if (transportation == null) {
            return null;
        }
        return TransportationResponse.builder()
            .id(transportation.getId())
            .originLocation(LocationResponse.from(transportation.getOriginLocation()))
            .destinationLocation(LocationResponse.from(transportation.getDestinationLocation()))
            .transportationType(transportation.getTransportationType())
            .operatingDays(DayOfWeekBitmaskMapper.toDays(transportation.getOperatingDays()))
            .build();
    }
}
