package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.aviation.routing.flight.path.engine.application.port.out.LocationProviderPort;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.repository.JpaLocationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationProviderAdapter implements LocationProviderPort {
    private final JpaLocationRepository locationRepository;

    @Override
    public Map<Long, String> getLocationNames(Set<Long> locationIds) {
        if (CollectionUtils.isEmpty(locationIds)) {
            return Map.of();
        }

        return locationRepository.findAllById(locationIds).stream()
                .collect(Collectors.toMap(
                    LocationEntity::getId,
                        entity -> entity.getName() + " (" + entity.getLocationCode() + ")"
                ));
    }
}