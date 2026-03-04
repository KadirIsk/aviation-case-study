package com.aviation.routing.flight.path.engine.domain.service.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record GraphProviderRequest(LocalDate date) {
}
