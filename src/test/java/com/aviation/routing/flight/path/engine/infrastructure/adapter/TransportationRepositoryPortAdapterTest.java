package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.TransportationFilterRequest;
import com.aviation.routing.flight.path.engine.domain.model.EventType;
import com.aviation.routing.flight.path.engine.domain.model.GraphSyncEvent;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.repository.JpaTransportationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class TransportationRepositoryPortAdapterTest {

    @Mock
    private JpaTransportationRepository jpaTransportationRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TransportationPersistenceAdapter adapter;

    @Test
    void save_buildsEntityFromDomain_savesAndMapsBack() {
        Transportation input = Transportation.builder()
            .originLocationId(1L)
            .destinationLocationId(2L)
            .transportationType("FLIGHT")
            .operatingDays((short)1)
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
        assertEquals((short)1, passed.getOperatingDays());

        assertNotNull(saved);
        assertEquals(200L, saved.getId());
        assertEquals(1L, saved.getOriginLocationId());
        assertEquals(2L, saved.getDestinationLocationId());
        assertEquals("FLIGHT", saved.getTransportationType());
        assertEquals((short)1, saved.getOperatingDays());

        ArgumentCaptor<GraphSyncEvent> eventCaptor = ArgumentCaptor.forClass(GraphSyncEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        GraphSyncEvent event = eventCaptor.getValue();
        assertEquals(1L, event.originId());
        assertEquals("2:FLIGHT", event.destinationCompositeId());
        assertEquals("FLIGHT:1", event.value());
        assertEquals(EventType.SAVE, event.eventType());
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        TransportationEntity entity = TransportationEntity.builder()
            .id(15L)
            .originLocationEntityId(10L)
            .destinationLocationEntityId(20L)
            .transportationType("FLIGHT")
            .operatingDays((short)1)
            .build();

        when(jpaTransportationRepository.findById(15L)).thenReturn(Optional.of(entity));

        adapter.delete(15L);

        verify(jpaTransportationRepository).findById(15L);
        verify(jpaTransportationRepository).delete(entity);
        
        ArgumentCaptor<GraphSyncEvent> eventCaptor = ArgumentCaptor.forClass(GraphSyncEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        GraphSyncEvent event = eventCaptor.getValue();
        assertEquals(10L, event.originId());
        assertEquals("20:FLIGHT", event.destinationCompositeId());
        assertEquals(EventType.DELETE, event.eventType());
    }

    @Test
    void update_updatesOperatingDays_andSaves() {
        Transportation input = Transportation.builder()
            .id(100L)
            .operatingDays((short) 5)
            .build();

        TransportationEntity existing = TransportationEntity.builder()
            .id(100L)
            .originLocationEntityId(1L)
            .destinationLocationEntityId(2L)
            .transportationType("FLIGHT")
            .operatingDays((short) 1)
            .build();

        when(jpaTransportationRepository.findById(100L)).thenReturn(Optional.of(existing));
        when(jpaTransportationRepository.save(any(TransportationEntity.class))).thenAnswer(i -> i.getArgument(0));

        Transportation result = adapter.update(input);

        assertEquals((short) 5, existing.getOperatingDays());
        verify(jpaTransportationRepository).save(existing);
        assertNotNull(result);
        
        verify(eventPublisher).publishEvent(any(GraphSyncEvent.class));
    }

    @Test
    void findById_whenFound_mapsToDomain() {
        TransportationEntity entity = TransportationEntity.builder()
            .id(9L)
            .originLocationEntityId(3L)
            .destinationLocationEntityId(4L)
            .transportationType("BUS")
            .operatingDays((short)1)
            .build();

        when(jpaTransportationRepository.findById(9L)).thenReturn(Optional.of(entity));

        Transportation result = adapter.get(9L);

        assertNotNull(result);
        assertEquals(9L, result.getId());
        assertEquals(3L, result.getOriginLocationId());
        assertEquals(4L, result.getDestinationLocationId());
        verify(jpaTransportationRepository).findById(9L);
    }

    @Test
    void findAll_mapsAllEntitiesToDomain() {
        TransportationFilterRequest filter = TransportationFilterRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        List<TransportationEntity> entities = List.of(
            TransportationEntity.builder().id(1L).originLocationEntityId(10L).destinationLocationEntityId(11L)
                .transportationType("FLIGHT").operatingDays((short)1).build(),
            TransportationEntity.builder().id(2L).originLocationEntityId(12L).destinationLocationEntityId(13L)
                .transportationType("BUS").operatingDays((short)1).build()
        );

        when(jpaTransportationRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(entities, pageable, entities.size()));

        Page<Transportation> page = adapter.getTransportations(filter, pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getContent().size());

        Transportation first = page.getContent().getFirst();
        assertEquals(1L, first.getId());
        assertEquals(10L, first.getOriginLocationId());
        assertEquals(11L, first.getDestinationLocationId());
        assertEquals("FLIGHT", first.getTransportationType());
        assertEquals((short)1, first.getOperatingDays());

        Transportation second = page.getContent().get(1);
        assertEquals(2L, second.getId());
        assertEquals(12L, second.getOriginLocationId());
        assertEquals(13L, second.getDestinationLocationId());
        assertEquals("BUS", second.getTransportationType());
        assertEquals((short)1, second.getOperatingDays());

        verify(jpaTransportationRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}