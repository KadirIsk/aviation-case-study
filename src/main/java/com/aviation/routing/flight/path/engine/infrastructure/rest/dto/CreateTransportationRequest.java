package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import java.time.DayOfWeek;
import java.util.Set;

import com.aviation.routing.flight.path.engine.application.dto.CreateTransportationUseCase;
import com.aviation.routing.flight.path.engine.common.util.DayOfWeekBitmaskMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateTransportationRequest(
    @Schema(description = "ID of the origin location", example = "1")
    @NotNull @Positive Long originLocationId,
    @Schema(description = "ID of the destination location", example = "2")
    @NotNull @Positive Long destinationLocationId,
    @Schema(description = "Type of the transportation", example = "FLIGHT, BUS, SUBWAY, UBER")
    @NotBlank String transportationType,
    @Schema(description = "Operating days of the transportation", type = "array", example = "[\"MONDAY\", \"TUESDAY\"]")
    Set<DayOfWeek> operatingDays
) {
    public CreateTransportationUseCase toUseCase() {
        return new CreateTransportationUseCase(
            this.originLocationId(),
            this.destinationLocationId(),
            this.transportationType(),
            DayOfWeekBitmaskMapper.toBitmask(this.operatingDays())
        );
    }
}