package com.aviation.routing.flight.path.engine.domain.model;

import lombok.Builder;

@Builder
public record GraphSyncEvent(
    Long originId,
    String destinationCompositeId,
    String value,
    EventType eventType
) { }