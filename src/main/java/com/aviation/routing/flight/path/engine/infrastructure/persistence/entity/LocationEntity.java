package com.aviation.routing.flight.path.engine.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locations_seq_gen")
    @SequenceGenerator(name = "locations_seq_gen", sequenceName = "locations_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Location name cannot be blank")
    @Size(max = 255, message = "Location name must be less than 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 255, message = "Country must be less than 255 characters")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 255, message = "City must be less than 255 characters")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "Location code cannot be blank")
    @Size(max = 10, message = "Location code must be less than 10 characters")
    @Column(name = "location_code", nullable = false, length = 10)
    private String locationCode;
}