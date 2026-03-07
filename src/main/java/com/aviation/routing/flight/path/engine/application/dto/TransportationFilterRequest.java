package com.aviation.routing.flight.path.engine.application.dto;

import java.time.DayOfWeek;
import java.util.Set;

import lombok.Builder;

@Builder
public record TransportationFilterRequest(
    Long originLocationId,
    Long destinationLocationId,
    String transportationType,
    Set<DayOfWeek> operatingDays
) { }