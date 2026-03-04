package com.aviation.routing.flight.path.engine.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RouteFacade {
    private final RouteFinder routeFinder;


}
