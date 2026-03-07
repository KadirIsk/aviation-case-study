package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import java.time.DayOfWeek;
import java.util.Set;

import com.aviation.routing.flight.path.engine.application.dto.UpdateTransportationUseCase;
import com.aviation.routing.flight.path.engine.common.util.DayOfWeekBitmaskMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateTransportationRequest(
    @Schema(description = "Operating days of the transportation", example = "Monday, Tuesday")
    @NotNull Set<DayOfWeek> operatingDays
) {
    public UpdateTransportationUseCase toUseCase(Long id) {
        return new UpdateTransportationUseCase(
            id,
            DayOfWeekBitmaskMapper.toBitmask(this.operatingDays())
        );
    }
}