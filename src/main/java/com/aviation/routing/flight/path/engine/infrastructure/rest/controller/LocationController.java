package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import com.aviation.routing.flight.path.engine.application.dto.LocationFilterRequest;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.common.payload.ApiResponse;
import com.aviation.routing.flight.path.engine.common.payload.PageData;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.CreateLocationRequest;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.LocationResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.UpdateLocationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "Location Management", description = "APIs for managing locations")
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "Create a new location", description = "Creates a new location with the given details")
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Location created successfully"
            )
        }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<LocationResponse>> create(@Valid @RequestBody CreateLocationRequest request) {
        Location location = locationService.create(request.toUseCase());

        ApiResponse<LocationResponse> apiResponse = ApiResponse.success(
            LocationResponse.from(location),
            "Location created successfully"
        );

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get location by ID", description = "Returns a single location by its ID")
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Location retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Location not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponse>> getById(@PathVariable Long id) {
        Location location = locationService.get(id);

        ApiResponse<LocationResponse> apiResponse = ApiResponse.success(
            LocationResponse.from(location),
            "Location retrieved successfully"
        );

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get filtered locations", description = "Returns a paginated list of locations based on filters")
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Locations retrieved successfully"
            )
        }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageData<LocationResponse>>> getAll(
        @ParameterObject LocationFilterRequest filterRequest,
        @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<Location> locationPage = locationService.get(filterRequest, pageable);

        PageData<LocationResponse> pageData = PageData.from(locationPage.map(LocationResponse::from));

        return ResponseEntity.ok(ApiResponse.success(pageData, "Locations retrieved successfully"));
    }

    @Operation(summary = "Update an existing location", description = "Updates the location with the given ID")
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Location updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Location not found")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponse>> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateLocationRequest request
    ) {
        Location location = locationService.update(request.toUseCase(id));

        ApiResponse<LocationResponse> apiResponse = ApiResponse.success(
            LocationResponse.from(location),
            "Location updated successfully"
        );

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Delete a location", description = "Deletes the location with the given ID")
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "Location deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Location not found")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
