package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import com.aviation.routing.flight.path.engine.application.port.in.FindFlightRoutesUseCase;
import com.aviation.routing.flight.path.engine.common.payload.ApiResponse;
import com.aviation.routing.flight.path.engine.common.util.DayOfWeekBitmaskMapper;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.RouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Tag(name = "Route Finder", description = "APIs for finding flight routes between locations")
public class RouteController {

    private final FindFlightRoutesUseCase findFlightRoutesUseCase;

    @Operation(summary = "Find routes", description = "Finds available flight routes between origin and destination on requested days")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Routes retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteResponse>>> findRoutes(
            @Parameter(description = "ID of the origin location", example = "1")
            @RequestParam("originId") Long originId,
            @Parameter(description = "ID of the destination location", example = "2")
            @RequestParam("destinationId") Long destinationId,
            @Parameter(description = "Requested operating days")
            @RequestParam("requestedDayMask") Set<DayOfWeek> operatingDays) {

        List<RouteResponse> routes = findFlightRoutesUseCase.execute(
            originId,
            destinationId,
            DayOfWeekBitmaskMapper.toBitmask(operatingDays)
        );
        
        return ResponseEntity.ok(ApiResponse.success(routes, "Routes retrieved successfully"));
    }
}
