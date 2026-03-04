package com.aviation.routing.flight.path.engine.domain.service;

import com.aviation.routing.flight.path.engine.domain.model.route.finder.Route;

public interface RouteFilter {
    boolean isSatisfiedBy(Route route);
}