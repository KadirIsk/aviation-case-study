package com.aviation.routing.flight.path.engine.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LocationRequest(
    @Schema(description = "Name of the location", example = "Taksim Meydani")
    @NotBlank String name,
    @Schema(description = "Country of the location", example = "Turkey")
    @NotBlank String country,
    @Schema(description = "City of the location", example = "Istanbul")
    @NotBlank String city,
    @Schema(description = "Location code", example = "IST")
    @NotBlank @Size(max = 10) String locationCode
) { }