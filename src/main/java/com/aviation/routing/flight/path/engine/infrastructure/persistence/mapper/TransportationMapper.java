package com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper;

import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransportationMapper {

    public static Transportation toDomain(TransportationEntity entity) {
        if (entity == null) {
            return null;
        }

        return Transportation.builder()
            .id(entity.getId())
            .originLocationId(entity.getOriginLocationEntityId())
            .destinationLocationId(entity.getDestinationLocationEntityId())
            .transportationType(entity.getTransportationType())
            .operatingDays(entity.getOperatingDays())
            .build();
    }

    public static TransportationEntity toEntity(Transportation domain) {
        if (domain == null) {
            return null;
        }

        return TransportationEntity.builder()
            .id(domain.getId())
            .originLocationEntityId(domain.getOriginLocationId())
            .destinationLocationEntityId(domain.getDestinationLocationId())
            .transportationType(domain.getTransportationType())
            .operatingDays(domain.getOperatingDays())
            .build();
    }
}

