package com.aviation.routing.flight.path.engine.infrastructure.config;

import java.util.List;

import com.aviation.routing.flight.path.engine.domain.port.RouteFilterPort;
import com.aviation.routing.flight.path.engine.domain.port.RouteRulePort;
import com.aviation.routing.flight.path.engine.domain.service.impl.FlightTransferLimitRule;
import com.aviation.routing.flight.path.engine.domain.service.impl.MaxTransportationRule;
import com.aviation.routing.flight.path.engine.domain.service.impl.RouteFilterService;
import com.aviation.routing.flight.path.engine.domain.service.impl.SingleFlightRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public RouteFilterPort routeFilterPort(List<RouteRulePort> rules) {
        return new RouteFilterService(rules);
    }

    @Bean
    public RouteRulePort singleFlightRule() {
        return new SingleFlightRule();
    }

    @Bean
    public RouteRulePort maxTransportationRule() {
        return new MaxTransportationRule();
    }

    @Bean
    public RouteRulePort flightTransferLimitRule() {
        return new FlightTransferLimitRule();
    }
}