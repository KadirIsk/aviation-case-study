package com.aviation.routing.flight.path.engine.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalCacheConfig {
    private static final int MAXIMUM_SIZE = 50_000;
    private static final int DURATION = 24;

    @Bean
    public Cache<Long, Map<Long, EdgeInfo>> edgeLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .expireAfterWrite(DURATION, TimeUnit.HOURS)
                .recordStats()
                .build();
    }
}