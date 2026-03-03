package com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import org.junit.jupiter.api.Test;

class TransportationMapperTest {

    @Test
    void toDomain_whenNull_returnsNull() {
        assertNull(TransportationMapper.toDomain(null));
    }

    @Test
    void toEntity_whenNull_returnsNull() {
        assertNull(TransportationMapper.toEntity(null));
    }

    @Test
    void toDomain_mapsAllFields() {
        TransportationEntity entity = TransportationEntity.builder()
            .id(10L)
            .originLocationEntityId(1L)
            .destinationLocationEntityId(2L)
            .transportationType("FLIGHT")
            .operatingDays("MON-FRI")
            .build();

        Transportation domain = TransportationMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(10L, domain.getId());
        assertEquals(1L, domain.getOriginLocationId());
        assertEquals(2L, domain.getDestinationLocationId());
        assertEquals("FLIGHT", domain.getTransportationType());
        assertEquals("MON-FRI", domain.getOperatingDays());
    }

    @Test
    void toEntity_mapsAllFields() {
        Transportation domain = Transportation.builder()
            .id(11L)
            .originLocationId(3L)
            .destinationLocationId(4L)
            .transportationType("BUS")
            .operatingDays("DAILY")
            .build();

        TransportationEntity entity = TransportationMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(11L, entity.getId());
        assertEquals(3L, entity.getOriginLocationEntityId());
        assertEquals(4L, entity.getDestinationLocationEntityId());
        assertEquals("BUS", entity.getTransportationType());
        assertEquals("DAILY", entity.getOperatingDays());
    }
}