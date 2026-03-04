package com.aviation.routing.flight.path.engine.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Entity
@Table(name = "transportations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transportations_seq_gen")
    @SequenceGenerator(name = "transportations_seq_gen", sequenceName = "transportations_seq", allocationSize = 1)
    private Long id;

    @Column(name = "origin_location_id", nullable = false)
    private Long originLocationEntityId;

    @NotNull(message = "Origin location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_location_id", insertable = false, updatable = false)
    private LocationEntity originLocationEntity;

    @Column(name = "destination_location_id", nullable = false)
    private Long destinationLocationEntityId;

    @NotNull(message = "Destination location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_location_id", insertable = false, updatable = false)
    private LocationEntity destinationLocationEntity;

    @NotBlank(message = "Transportation type cannot be blank")
    @Size(max = 50, message = "Transportation type must be less than 50 characters")
    @Column(name = "transportation_type", nullable = false, length = 50)
    private String transportationType;

    @Column(name = "operating_days", nullable = false, length = 20)
    private Short operatingDays;
}