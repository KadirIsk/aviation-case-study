package com.aviation.routing.flight.path.engine.infrastructure.listener;

import com.aviation.routing.flight.path.engine.domain.model.EventType;
import com.aviation.routing.flight.path.engine.domain.model.GraphSyncEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class GraphStateSyncListener {

    private final RedissonClient redissonClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGraphSyncAfterCommit(GraphSyncEvent event) {
        try {
            if (EventType.SAVE.equals(event.eventType())) {
                redissonClient.getMap("node:edges:" + event.originId())
                              .put(event.destinationCompositeId(), event.value());
            }

            if (EventType.DELETE.equals(event.eventType())) {
                redissonClient.getMap("node:edges:" + event.originId())
                              .remove(event.destinationCompositeId());
            }

            redissonClient.getTopic("graph-invalidation-topic").publish(event.originId());
            
        } catch (Exception e) {
            log.error("Failed to sync Redis after DB commit for Origin: {}", event.originId(), e);
        }
    }
}