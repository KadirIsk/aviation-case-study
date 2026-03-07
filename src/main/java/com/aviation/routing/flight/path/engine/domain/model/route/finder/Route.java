package com.aviation.routing.flight.path.engine.domain.model.route.finder;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Route information")
public class Route {
    @Schema(description = "Title of the route", example = "IST-LHR")
    private String title;
    @Schema(description = "Steps to follow for this route")
    private List<RouteStep> steps;
}
