package com.aviation.routing.flight.path.engine.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import org.junit.jupiter.api.Test;

class LocationMapperTest {

    @Test
    void toDomain_whenNull_returnsNull() {
        assertNull(LocationMapper.toDomain(null));
    }

    @Test
    void toEntity_whenNull_returnsNull() {
        assertNull(LocationMapper.toEntity(null));
    }

    @Test
    void toDomain_mapsAllFields() {
        LocationEntity entity = LocationEntity.builder()
            .id(1L)
            .name("Sabiha Gokcen")
            .country("TR")
            .city("Istanbul")
            .locationCode("SAW")
            .build();

        Location domain = LocationMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("Sabiha Gokcen", domain.getName());
        assertEquals("TR", domain.getCountry());
        assertEquals("Istanbul", domain.getCity());
        assertEquals("SAW", domain.getLocationCode());
    }

    @Test
    void toEntity_mapsAllFields() {
        Location domain = Location.builder()
            .id(2L)
            .name("Esenboga")
            .country("TR")
            .city("Ankara")
            .locationCode("ESB")
            .build();

        LocationEntity entity = LocationMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Esenboga", entity.getName());
        assertEquals("TR", entity.getCountry());
        assertEquals("Ankara", entity.getCity());
        assertEquals("ESB", entity.getLocationCode());
    }
}