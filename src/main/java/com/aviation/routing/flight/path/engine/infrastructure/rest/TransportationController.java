package com.aviation.routing.flight.path.engine.infrastructure.rest;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    // todo: tum api uclari tek tip bir response donmeli!!!
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transportation create(@Valid @RequestBody TransportationRequest request) {
        return transportationService.createTransportation(request);
    }

    @GetMapping("/{id}")
    public Transportation getById(@PathVariable Long id) {
        return transportationService.getTransportation(id);
    }

    // todo: page'i komple donme
    @Operation(summary = "Get filtered transportations", description = "Returns a paginated list of transportations based on filters")
    @GetMapping
    public Page<Transportation> getAll(TransportationRequest request, Pageable pageable) {
        return transportationService.getTransportations(request, pageable);
    }

    @PutMapping("/{id}")
    public Transportation update(@PathVariable Long id, @Valid @RequestBody TransportationRequest request) {
        return transportationService.updateTransportation(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        transportationService.deleteTransportation(id);
    }
}