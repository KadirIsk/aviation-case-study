package com.aviation.routing.flight.path.engine.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Login Request")
public record LoginRequest(
    @Schema(description = "Username", example = "admin")
    @NotBlank String username,
    @Schema(description = "Password", example = "admin123")
    @NotBlank String password
) { }
