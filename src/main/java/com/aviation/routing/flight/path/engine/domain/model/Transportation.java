package com.aviation.routing.flight.path.engine.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transportation {
    private Long id;
    private Long originLocationId;
    private Long destinationLocationId;
    private String transportationType;
    private String operatingDays;
}