package com.aviation.routing.flight.path.engine.domain.model;

import lombok.Builder;

@Builder
public record GraphSyncEvent(
    Long originId,
    Long destinationId,
    String value,
    EventType eventType
) { }