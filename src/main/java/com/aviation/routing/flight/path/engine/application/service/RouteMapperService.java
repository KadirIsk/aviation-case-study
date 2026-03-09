package com.aviation.routing.flight.path.engine.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.aviation.routing.flight.path.engine.application.port.in.RouteMapperPort;
import com.aviation.routing.flight.path.engine.application.port.out.LocationProviderPort;
import com.aviation.routing.flight.path.engine.domain.model.projection.PathEdge;
import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse.RouteDto;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse.RouteStepDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteMapperService implements RouteMapperPort {
    private final LocationProviderPort locationProviderPort;

    @Override
    public List<RouteResponse> mapToResponses(List<RouteCandidate> validRoutes) {
        if (CollectionUtils.isEmpty(validRoutes)) {
            return Collections.emptyList();
        }

        Set<Long> allNodeIds = extractUniqueNodeIds(validRoutes);

        Map<Long, String> locationNames = locationProviderPort.getLocationNames(allNodeIds);

        return validRoutes.stream()
            .map(candidate -> buildRouteResponse(candidate, locationNames))
            .toList();
    }

    private Set<Long> extractUniqueNodeIds(List<RouteCandidate> routes) {
        return routes.stream()
            .flatMap(route -> route.edges().stream())
            .map(edge -> Set.of(edge.originId(), edge.destinationId()))
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    private RouteResponse buildRouteResponse(RouteCandidate candidate, Map<Long, String> locationNames) {
        List<RouteResponse.RouteStepDto> steps = new ArrayList<>();
        String title = "Unknown Route";

        boolean titleSet = false;

        for (PathEdge edge : candidate.edges()) {
            String originName = locationNames.getOrDefault(edge.originId(), "Unknown Location");
            String destName = locationNames.getOrDefault(edge.destinationId(), "Unknown Location");
            String type = edge.transportationType();

            if (!titleSet && "FLIGHT".equalsIgnoreCase(type)) {
                title = "via " + originName;
                titleSet = true;
            }

            steps.add(RouteStepDto.builder()
                          .origin(originName)
                          .destination(destName)
                          .transportationType(type)
                          .build());
        }

        return RouteResponse.builder()
            .route(RouteDto.builder()
                       .title(title)
                       .steps(steps)
                       .build())
            .build();
    }
}