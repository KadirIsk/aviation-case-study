package com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper;

import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocationMapper {

    public static Location toDomain(LocationEntity entity) {
        if (entity == null) {
            return null;
        }

        return Location.builder()
            .id(entity.getId())
            .name(entity.getName())
            .country(entity.getCountry())
            .city(entity.getCity())
            .locationCode(entity.getLocationCode())
            .build();
    }

    public static LocationEntity toEntity(Location domain) {
        if (domain == null) {
            return null;
        }

        return LocationEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .country(domain.getCountry())
            .city(domain.getCity())
            .locationCode(domain.getLocationCode())
            .build();
    }
}

