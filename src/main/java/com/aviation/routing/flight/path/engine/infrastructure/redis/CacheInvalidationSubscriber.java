package com.aviation.routing.flight.path.engine.infrastructure.redis;

import java.util.Map;

import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheInvalidationSubscriber {

    private final RedissonClient redissonClient;
    private final Cache<Long, Map<String, EdgeInfo>> localCache;

    @PostConstruct
    public void subscribe() {
        redissonClient.getTopic("graph-invalidation-topic")
                .addListener(Long.class, (channel, originNodeId) -> localCache.invalidate(originNodeId));
    }
}