package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.port.in.FindFlightRoutesUseCase;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.RouteResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

    private final FindFlightRoutesUseCase findFlightRoutesUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteResponse>>> findRoutes(
            @RequestParam("originId") Long originId,
            @RequestParam("destinationId") Long destinationId,
            @RequestParam("requestedDayMask") short requestedDayMask) { // todo: burasini sonradan gun alacak sekilde update edelim

        List<RouteResponse> routes = findFlightRoutesUseCase.execute(originId, destinationId, requestedDayMask);
        
        return ResponseEntity.ok(ApiResponse.success(routes, "Routes retrieved successfully"));
    }
}