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

import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.JpaTransportationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransportationRepositoryAdapterTest {

    @Mock
    private JpaTransportationRepository jpaTransportationRepository;

    @InjectMocks
    private TransportationRepositoryAdapter adapter;

    @Test
    void save_buildsEntityFromDomain_savesAndMapsBack() {
        Transportation input = Transportation.builder()
            .originLocationId(1L)
            .destinationLocationId(2L)
            .transportationType("FLIGHT")
            .operatingDays("MON-FRI")
            .build();

        when(jpaTransportationRepository.save(any(TransportationEntity.class)))
            .thenAnswer(invocation -> {
                TransportationEntity e = invocation.getArgument(0);

                return TransportationEntity.builder()
                    .id(200L)
                    .originLocationEntityId(e.getOriginLocationEntityId())
                    .destinationLocationEntityId(e.getDestinationLocationEntityId())
                    .transportationType(e.getTransportationType())
                    .operatingDays(e.getOperatingDays())
                    .build();
            });

        Transportation saved = adapter.save(input);

        ArgumentCaptor<TransportationEntity> captor = ArgumentCaptor.forClass(TransportationEntity.class);
        verify(jpaTransportationRepository).save(captor.capture());

        TransportationEntity passed = captor.getValue();
        assertNull(passed.getId());
        assertEquals(1L, passed.getOriginLocationEntityId());
        assertEquals(2L, passed.getDestinationLocationEntityId());
        assertEquals("FLIGHT", passed.getTransportationType());
        assertEquals("MON-FRI", passed.getOperatingDays());

        assertNotNull(saved);
        assertEquals(200L, saved.getId());
        assertEquals(1L, saved.getOriginLocationId());
        assertEquals(2L, saved.getDestinationLocationId());
        assertEquals("FLIGHT", saved.getTransportationType());
        assertEquals("MON-FRI", saved.getOperatingDays());
    }

    @Test
    void findById_whenFound_mapsToDomain() {
        TransportationEntity entity = TransportationEntity.builder()
            .id(9L)
            .originLocationEntityId(3L)
            .destinationLocationEntityId(4L)
            .transportationType("BUS")
            .operatingDays("DAILY")
            .build();

        when(jpaTransportationRepository.findById(9L)).thenReturn(Optional.of(entity));

        Optional<Transportation> result = adapter.findById(9L);

        assertTrue(result.isPresent());
        assertEquals(9L, result.get().getId());
        assertEquals(3L, result.get().getOriginLocationId());
        assertEquals(4L, result.get().getDestinationLocationId());
        verify(jpaTransportationRepository).findById(9L);
    }

    @Test
    void findAll_mapsAllEntitiesToDomain() {
        when(jpaTransportationRepository.findAll()).thenReturn(List.of(
            TransportationEntity.builder().id(1L).originLocationEntityId(10L).destinationLocationEntityId(11L)
                .transportationType("FLIGHT").operatingDays("DAILY").build(),
            TransportationEntity.builder().id(2L).originLocationEntityId(12L).destinationLocationEntityId(13L)
                .transportationType("BUS").operatingDays("WEEKEND").build()
        ));

        List<Transportation> all = adapter.findAll();

        assertEquals(2, all.size());
        assertEquals(1L, all.get(0).getId());
        assertEquals("FLIGHT", all.get(0).getTransportationType());
        assertEquals(2L, all.get(1).getId());
        assertEquals("BUS", all.get(1).getTransportationType());
        verify(jpaTransportationRepository).findAll();
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        adapter.deleteById(15L);

        verify(jpaTransportationRepository).deleteById(15L);
        verifyNoMoreInteractions(jpaTransportationRepository);
    }
}