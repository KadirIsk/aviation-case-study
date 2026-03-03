package com.aviation.routing.flight.path.engine.infrastructure.rest;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.domain.model.Location;
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
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    // todo: tum api uclari tek tip bir response donmeli!!!
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Location create(@Valid @RequestBody LocationRequest request) {
        return locationService.createLocation(request);
    }

    @GetMapping("/{id}")
    public Location getById(@PathVariable Long id) {
        return locationService.getLocation(id);
    }

    // todo: page'i komple donme
    @GetMapping
    public Page<Location> getAll(LocationRequest request, Pageable pageable) {
        return locationService.getLocations(request, pageable);
    }

    @PutMapping("/{id}")
    public Location update(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        return locationService.updateLocation(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}