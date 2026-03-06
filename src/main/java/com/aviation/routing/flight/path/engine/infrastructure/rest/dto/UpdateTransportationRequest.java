package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import com.aviation.routing.flight.path.engine.application.dto.UpdateTransportationUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record UpdateTransportationRequest(
    @Schema(description = "Operating days of the transportation", example = "Monday, Tuesday")
    @NotNull @Positive Short operatingDays // todo: belki string olarak alinip, short'a cevrilebilir
) {
    public UpdateTransportationUseCase toUseCase(Long id) {
        return new UpdateTransportationUseCase(
            id,
            this.operatingDays()
        );
    }
}