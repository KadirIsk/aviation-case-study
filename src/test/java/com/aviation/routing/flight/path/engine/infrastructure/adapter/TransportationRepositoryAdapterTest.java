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

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.JpaTransportationRepository;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
        TransportationRequest filter = TransportationRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        List<TransportationEntity> entities = List.of(
            TransportationEntity.builder().id(1L).originLocationEntityId(10L).destinationLocationEntityId(11L)
                .transportationType("FLIGHT").operatingDays("DAILY").build(),
            TransportationEntity.builder().id(2L).originLocationEntityId(12L).destinationLocationEntityId(13L)
                .transportationType("BUS").operatingDays("WEEKEND").build()
        );

        when(jpaTransportationRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(entities, pageable, entities.size()));

        Page<Transportation> page = adapter.findAll(filter, pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getContent().size());

        Transportation first = page.getContent().getFirst();
        assertEquals(1L, first.getId());
        assertEquals(10L, first.getOriginLocationId());
        assertEquals(11L, first.getDestinationLocationId());
        assertEquals("FLIGHT", first.getTransportationType());
        assertEquals("DAILY", first.getOperatingDays());

        Transportation second = page.getContent().get(1);
        assertEquals(2L, second.getId());
        assertEquals(12L, second.getOriginLocationId());
        assertEquals(13L, second.getDestinationLocationId());
        assertEquals("BUS", second.getTransportationType());
        assertEquals("WEEKEND", second.getOperatingDays());

        verify(jpaTransportationRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        adapter.deleteById(15L);

        verify(jpaTransportationRepository).deleteById(15L);
        verifyNoMoreInteractions(jpaTransportationRepository);
    }
}