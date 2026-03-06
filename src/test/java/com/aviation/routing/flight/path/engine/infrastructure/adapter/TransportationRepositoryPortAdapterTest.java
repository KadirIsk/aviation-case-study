package com.aviation.routing.flight.path.engine.infrastructure.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.CreateTransportationUseCase;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.repository.JpaTransportationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
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
    private RedissonClient redissonClient;

    @Mock
    private RMap<Long, String> transportationMap;

    @Mock
    private RTopic invalidationTopic;

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
        doReturn(transportationMap).when(redissonClient).getMap("node:edges:1");
        doReturn(invalidationTopic).when(redissonClient).getTopic("graph-invalidation-topic");

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

        verify(redissonClient).getMap("node:edges:1");
        verify(transportationMap).put(2L, "FLIGHT:1");
        verify(redissonClient).getTopic("graph-invalidation-topic");
        verify(invalidationTopic).publish(1L);
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
        doReturn(transportationMap).when(redissonClient).getMap("node:edges:10");
        doReturn(invalidationTopic).when(redissonClient).getTopic("graph-invalidation-topic");

        adapter.delete(15L);

        verify(jpaTransportationRepository).findById(15L);
        verify(jpaTransportationRepository).deleteById(15L);
        verify(redissonClient).getMap("node:edges:10");
        verify(transportationMap).remove(20L);
        verify(redissonClient).getTopic("graph-invalidation-topic");
        verify(invalidationTopic).publish(10L);
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
        CreateTransportationUseCase filter = CreateTransportationUseCase.builder().build();
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