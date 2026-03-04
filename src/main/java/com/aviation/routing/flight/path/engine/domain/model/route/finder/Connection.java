package com.aviation.routing.flight.path.engine.domain.model.route.finder;

public record Connection<T>(Vertex<T> target, EdgeInfo info) {}