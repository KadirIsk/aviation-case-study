package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import com.aviation.routing.flight.path.engine.application.dto.CreateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.ApiResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.CreateLocationRequest;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.UpdateLocationRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Location>> create(@Valid @RequestBody CreateLocationRequest request) {
        Location location = locationService.createLocation(request.toUseCase());

        ApiResponse<Location> apiResponse = ApiResponse.success(location, "Location created successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Location>> getById(@PathVariable Long id) {
        Location location = locationService.getLocation(id);

        ApiResponse<Location> apiResponse = ApiResponse.success(location, "Location retrieved successfully");

        return ResponseEntity.ok(apiResponse);
    }

    // todo: page'i komple donme
    @Operation(summary = "Get filtered locations", description = "Returns a paginated list of locations based on filters")
    @GetMapping
    public Page<Location> getAll(CreateLocationUseCase request, Pageable pageable) {
        return locationService.getLocations(request, pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Location>> update(@PathVariable Long id, @Valid @RequestBody UpdateLocationRequest request) {
        Location location = locationService.updateLocation(request.toUseCase(id));

        ApiResponse<Location> apiResponse = ApiResponse.success(location, "Location updated successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}