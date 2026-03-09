package com.aviation.routing.flight.path.engine.infrastructure.listener;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aviation.routing.flight.path.engine.domain.model.EventType;
import com.aviation.routing.flight.path.engine.domain.model.GraphSyncEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
class GraphStateSyncListenerTest {

    @Mock
    private RedissonClient redissonClient;
    @Mock
    private RMap<Object, Object> rMap;
    @Mock
    private RTopic rTopic;

    @InjectMocks
    private GraphStateSyncListener listener;

    @Test
    void handleGraphSyncAfterCommit_whenSave_shouldPutToMapAndPublish() {
        GraphSyncEvent event = GraphSyncEvent.builder()
            .originId(1L)
            .destinationCompositeId("2:FLIGHT")
            .value("FLIGHT:1")
            .eventType(EventType.SAVE)
            .build();

        when(redissonClient.getMap("node:edges:1")).thenReturn(rMap);
        when(redissonClient.getTopic("graph-invalidation-topic")).thenReturn(rTopic);

        listener.handleGraphSyncAfterCommit(event);

        verify(rMap).put("2:FLIGHT", "FLIGHT:1");
        verify(rTopic).publish(1L);
    }

    @Test
    void handleGraphSyncAfterCommit_whenDelete_shouldRemoveFromMapAndPublish() {
        GraphSyncEvent event = GraphSyncEvent.builder()
            .originId(1L)
            .destinationCompositeId("2:FLIGHT")
            .eventType(EventType.DELETE)
            .build();

        when(redissonClient.getMap("node:edges:1")).thenReturn(rMap);
        when(redissonClient.getTopic("graph-invalidation-topic")).thenReturn(rTopic);

        listener.handleGraphSyncAfterCommit(event);

        verify(rMap).remove("2:FLIGHT");
        verify(rTopic).publish(1L);
    }

    @Test
    void handleGraphSyncAfterCommit_whenException_shouldLogAndNotThrow() {
        GraphSyncEvent event = GraphSyncEvent.builder()
            .originId(1L)
            .eventType(EventType.SAVE)
            .build();

        when(redissonClient.getMap(anyString())).thenThrow(new RuntimeException("Redis down"));

        // Should not throw exception
        listener.handleGraphSyncAfterCommit(event);
        
        verify(redissonClient).getMap(anyString());
    }
}
