package com.aviation.routing.flight.path.engine.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
    name = "locations", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name"),
    @UniqueConstraint(columnNames = "location_code")}
)
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
    @Size(max = 150, message = "Location name must be less than 150 characters")
    @Column(name = "name", unique = true, nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country must be less than 100 characters")
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City must be less than 100 characters")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank(message = "Location code cannot be blank")
    @Size(max = 10, message = "Location code must be less than 10 characters")
    @Column(name = "location_code", unique = true, nullable = false, length = 10)
    private String locationCode;
}