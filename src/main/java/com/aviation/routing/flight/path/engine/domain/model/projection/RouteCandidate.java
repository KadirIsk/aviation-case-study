package com.aviation.routing.flight.path.engine.domain.model.projection;

import java.util.List;

import lombok.Builder;

@Builder
public record RouteCandidate(List<PathEdge> edges) {
}