package com.aviation.routing.flight.path.engine.domain.service.impl;

import java.util.List;

import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.port.RouteFilterPort;
import com.aviation.routing.flight.path.engine.domain.port.RouteRulePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RouteFilterService implements RouteFilterPort {
    private final List<RouteRulePort> rules;

    @Override
    public List<RouteCandidate> filterValidRoutes(List<RouteCandidate> candidates) {
        return candidates.stream()
                .filter(candidate -> rules.stream().allMatch(rule -> rule.isSatisfiedBy(candidate)))
                .toList();
    }
}