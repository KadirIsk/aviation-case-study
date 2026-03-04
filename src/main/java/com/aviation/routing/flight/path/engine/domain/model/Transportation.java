package com.aviation.routing.flight.path.engine.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(of = {"id"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transportation {
    private Long id;
    private Long originLocationId;
    private Location originLocation;
    private Long destinationLocationId;
    private Location destinationLocation;
    private String transportationType;
    private Short operatingDays;
}