package com.aviation.routing.flight.path.engine.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LocationRequest(
    @NotBlank String name,
    @NotBlank String country,
    @NotBlank String city,
    @NotBlank @Size(max = 10) String locationCode
) { }