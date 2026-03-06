package com.aviation.routing.flight.path.engine.domain.model.route.finder;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class Route {
    private String title;
    private List<RouteStep> steps;
}
