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
public class Location {
    private Long id;
    private String name;
    private String country;
    private String city;
    private String locationCode;
}