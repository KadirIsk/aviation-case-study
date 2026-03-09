package com.aviation.routing.flight.path.engine.infrastructure.redis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatch;
import org.redisson.api.RLock;
import org.redisson.api.RMapAsync;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@ExtendWith(MockitoExtension.class)
class GraphInitializationServiceTest {

    @Mock
    private RedissonClient redissonClient;
    @Mock
    private TransportationService transportationService;
    @Mock
    private RLock lock;
    @Mock
    private RBatch batch;
    @Mock
    private RMapAsync<Object, Object> rMapAsync;
    @Mock
    private Slice<Transportation> slice;

    @InjectMocks
    private GraphInitializationService initializationService;

    @Test
    void initializeGraphOnStartup_whenLocked_shouldLoadData() throws InterruptedException {
        when(redissonClient.getLock("graph-init-lock")).thenReturn(lock);
        when(lock.tryLock(0, TimeUnit.SECONDS)).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        Transportation t = Transportation.builder()
            .originLocationId(1L)
            .destinationLocationId(2L)
            .transportationType("FLIGHT")
            .operatingDays((short) 1)
            .build();

        when(transportationService.findAllByOrderByOriginLocationId(any(Pageable.class))).thenReturn(slice);
        when(slice.getContent()).thenReturn(List.of(t));
        when(slice.hasNext()).thenReturn(false);

        when(redissonClient.createBatch(any(BatchOptions.class))).thenReturn(batch);
        when(batch.getMap(anyString())).thenReturn(rMapAsync);

        initializationService.initializeGraphOnStartup();

        verify(batch).execute();
        verify(lock).unlock();
    }

    @Test
    void initializeGraphOnStartup_whenAlreadyLocked_shouldSkip() throws InterruptedException {
        when(redissonClient.getLock("graph-init-lock")).thenReturn(lock);
        when(lock.tryLock(0, TimeUnit.SECONDS)).thenReturn(false);

        initializationService.initializeGraphOnStartup();

        verify(transportationService, never()).findAllByOrderByOriginLocationId(any());
    }
}
