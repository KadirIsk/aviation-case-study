package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Login Response")
public record LoginResponse(
    @Schema(description = "Access Token")
    String accessToken,
    @Schema(description = "Refresh Token")
    String refreshToken
) { }
