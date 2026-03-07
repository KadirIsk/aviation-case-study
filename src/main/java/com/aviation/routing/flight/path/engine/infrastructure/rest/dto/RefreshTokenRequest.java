package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Refresh Token Request")
public record RefreshTokenRequest(
    @Schema(description = "Refresh Token")
    @NotBlank String refreshToken
) { }
