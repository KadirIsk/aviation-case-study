package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.ApiResponse;
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
@RequestMapping("/api/v1/transportations")
@RequiredArgsConstructor
public class TransportationController {

    private final TransportationService transportationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Transportation>> create(@Valid @RequestBody TransportationRequest request) {
        Transportation transportation = transportationService.create(request);

        ApiResponse<Transportation> apiResponse = ApiResponse.success(transportation, "Transportation created successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transportation>> getById(@PathVariable Long id) {
        Transportation transportation = transportationService.get(id);

        ApiResponse<Transportation> apiResponse = ApiResponse.success(transportation, "Transportation retrieved successfully");

        return ResponseEntity.ok(apiResponse);
    }

    // todo: page'i komple donme
    @Operation(
        summary = "Get filtered transportations",
        description = "Returns a paginated list of transportations based on filters"
    )
    @GetMapping
    public Page<Transportation> getAll(TransportationRequest request, Pageable pageable) {
        return transportationService.getTransportations(request, pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Transportation>> update(
        @PathVariable Long id,
        @Valid @RequestBody TransportationRequest request
    ) {
        Transportation transportation = transportationService.update(id, request);

        ApiResponse<Transportation> apiResponse = ApiResponse.success(transportation, "Transportation updated successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transportationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}