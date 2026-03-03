package com.aviation.routing.flight.path.engine.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transportations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transportation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transportations_seq_gen")
    @SequenceGenerator(name = "transportations_seq_gen", sequenceName = "transportations_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "origin_location_id", nullable = false)
    private Location originLocation;

    @OneToOne
    @JoinColumn(name = "destination_location_id", nullable = false)
    private Location destinationLocation;

    @Column(name = "transportation_type", nullable = false, length = 50)
    private String transportationType;

    @Column(name = "operating_days", nullable = false, length = 20)
    private String operatingDays;
}