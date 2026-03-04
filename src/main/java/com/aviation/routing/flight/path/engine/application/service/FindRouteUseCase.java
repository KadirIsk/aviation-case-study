package com.aviation.routing.flight.path.engine.application.service;

import java.time.LocalDate;
import java.util.Collection;

import com.aviation.routing.flight.path.engine.domain.model.route.finder.Route;

public interface FindRouteUseCase {
    Collection<Route> findRoute(Long originId, Long destinationId, LocalDate date);
}
