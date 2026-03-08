package com.aviation.routing.flight.path.engine.infrastructure.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatch;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraphInitializationService {

    public static final int PAGE_SIZE = 10000;
    private final RedissonClient redissonClient;
    private final TransportationService transportationService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeGraphOnStartup() {
        RLock lock = redissonClient.getLock("graph-init-lock");

        try {
            boolean isLocked = lock.tryLock(0, TimeUnit.SECONDS);
            
            if (isLocked) {
                log.info("Graph initialization lock acquired. Starting data load to Redis...");
                loadGraphToRedis();
                log.info("Graph initialization completed successfully.");
            } else {
                log.info("Another pod is initializing the graph. Skipping...");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Graph initialization interrupted", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("Graph initialization lock released.");
            }
        }
    }

    private void loadGraphToRedis() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Slice<Transportation> slice;
        int totalProcessed = 0;

        do {
            slice = transportationService.findAllByOrderByOriginLocationId(pageable);
            List<Transportation> edges = slice.getContent();

            if (edges.isEmpty()) {
                break;
            }

            RBatch batch = redissonClient.createBatch(BatchOptions.defaults());

            for (Transportation edge : edges) {
                String redisKey = "node:edges:" + edge.getOriginLocationId();
                String value = edge.getTransportationType() + ":" + edge.getOperatingDays();

                String neighborKey = edge.getDestinationLocationId() + ":" + edge.getTransportationType();
                batch.getMap(redisKey).putAsync(neighborKey, value);
            }

            batch.execute();

            totalProcessed += edges.size();
            log.info("Processed {} edges. Total loaded to Redis: {}", edges.size(), totalProcessed);

            pageable = slice.nextPageable();

        } while (slice.hasNext());
    }
}