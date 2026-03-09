package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import com.aviation.routing.flight.path.engine.application.dto.TransportationFilterRequest;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.common.payload.ApiResponse;
import com.aviation.routing.flight.path.engine.common.payload.PageData;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.CreateTransportationRequest;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.TransportationResponse;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.UpdateTransportationRequest;
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
@RequestMapping("/api/v1/transportations")
@RequiredArgsConstructor
@Tag(name = "Transportation Management", description = "APIs for managing transportations")
public class TransportationController {

    private final TransportationService transportationService;

    @Operation(summary = "Create a new transportation", description = "Creates a new transportation with the given details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transportation created successfully")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<TransportationResponse>> create(@Valid @RequestBody CreateTransportationRequest request) {
        Transportation transportation = transportationService.create(request.toUseCase());

        ApiResponse<TransportationResponse> apiResponse = ApiResponse.success(TransportationResponse.from(transportation), "Transportation created successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get transportation by ID", description = "Returns a single transportation by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transportation retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transportation not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransportationResponse>> getById(@PathVariable Long id) {
        Transportation transportation = transportationService.get(id);

        ApiResponse<TransportationResponse> apiResponse = ApiResponse.success(TransportationResponse.from(transportation), "Transportation retrieved successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get filtered transportations", description = "Returns a paginated list of transportations based on filters")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transportations retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageData<TransportationResponse>>> getAll(
            @ParameterObject TransportationFilterRequest request,
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Transportation> transportations = transportationService.get(request, pageable);

        PageData<TransportationResponse> pageData = PageData.from(transportations.map(TransportationResponse::from));

        return ResponseEntity.ok(ApiResponse.success(pageData, "Transportations retrieved successfully"));
    }

    @Operation(summary = "Update an existing transportation", description = "Updates the transportation with the given ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transportation updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transportation not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransportationResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransportationRequest request
    ) {
        Transportation transportation = transportationService.update(request.toUseCase(id));

        ApiResponse<TransportationResponse> apiResponse = ApiResponse.success(TransportationResponse.from(transportation), "Transportation updated successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Delete a transportation", description = "Deletes the transportation with the given ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Transportation deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transportation not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transportationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
