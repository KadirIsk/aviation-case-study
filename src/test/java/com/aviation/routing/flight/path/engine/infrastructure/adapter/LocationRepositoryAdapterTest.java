package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.JpaLocationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationRepositoryAdapterTest {

    @Mock
    private JpaLocationRepository jpaLocationRepository;

    @InjectMocks
    private LocationRepositoryAdapter adapter;

    @Test
    void save_mapsDomainToEntity_savesAndMapsBack() {
        Location input = Location.builder()
            .name("Ataturk")
            .country("TR")
            .city("Istanbul")
            .locationCode("IST")
            .build();

        when(jpaLocationRepository.save(any(LocationEntity.class)))
            .thenAnswer(invocation -> {
                LocationEntity e = invocation.getArgument(0);

                return LocationEntity.builder()
                    .id(100L)
                    .name(e.getName())
                    .country(e.getCountry())
                    .city(e.getCity())
                    .locationCode(e.getLocationCode())
                    .build();
            });

        Location saved = adapter.save(input);

        ArgumentCaptor<LocationEntity> captor = ArgumentCaptor.forClass(LocationEntity.class);
        verify(jpaLocationRepository).save(captor.capture());

        LocationEntity passed = captor.getValue();
        assertNull(passed.getId());
        assertEquals("Ataturk", passed.getName());
        assertEquals("TR", passed.getCountry());
        assertEquals("Istanbul", passed.getCity());
        assertEquals("IST", passed.getLocationCode());

        assertNotNull(saved);
        assertEquals(100L, saved.getId());
        assertEquals("Ataturk", saved.getName());
        assertEquals("TR", saved.getCountry());
        assertEquals("Istanbul", saved.getCity());
        assertEquals("IST", saved.getLocationCode());
    }

    @Test
    void findById_whenFound_mapsToDomain() {
        LocationEntity entity = LocationEntity.builder()
            .id(5L)
            .name("SAW")
            .country("TR")
            .city("Istanbul")
            .locationCode("SAW")
            .build();

        when(jpaLocationRepository.findById(5L)).thenReturn(Optional.of(entity));

        Optional<Location> result = adapter.findById(5L);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getId());
        assertEquals("SAW", result.get().getName());
        verify(jpaLocationRepository).findById(5L);
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        when(jpaLocationRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Location> result = adapter.findById(99L);

        assertTrue(result.isEmpty());
        verify(jpaLocationRepository).findById(99L);
    }

    @Test
    void findAll_mapsAllEntitiesToDomain() {
        when(jpaLocationRepository.findAll()).thenReturn(List.of(
            LocationEntity.builder().id(1L).name("A").country("TR").city("X").locationCode("AAA").build(),
            LocationEntity.builder().id(2L).name("B").country("TR").city("Y").locationCode("BBB").build()
        ));

        List<Location> all = adapter.findAll();

        assertEquals(2, all.size());
        assertEquals(1L, all.get(0).getId());
        assertEquals("AAA", all.get(0).getLocationCode());
        assertEquals(2L, all.get(1).getId());
        assertEquals("BBB", all.get(1).getLocationCode());
        verify(jpaLocationRepository).findAll();
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        adapter.deleteById(7L);

        verify(jpaLocationRepository).deleteById(7L);
        verifyNoMoreInteractions(jpaLocationRepository);
    }
}