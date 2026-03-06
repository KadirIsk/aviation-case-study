package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import com.aviation.routing.flight.path.engine.application.dto.UpdateLocationUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateLocationRequest(
    @Schema(description = "Name of the location", example = "Taksim Meydani")
    @NotBlank String name
) {
    public UpdateLocationUseCase toUseCase(Long id) {
        return new UpdateLocationUseCase(
            id,
            this.name()
        );
    }
}
