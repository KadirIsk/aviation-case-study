package com.aviation.routing.flight.path.engine.infrastructure.persistence;

import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.Graph;
import com.aviation.routing.flight.path.engine.domain.service.TransitGraphProvider;
import com.aviation.routing.flight.path.engine.domain.service.dto.GraphProviderRequest;

public class MultimodalRuleBasedGraphProvider implements TransitGraphProvider<Location> {

    @Override
    public Graph<Location> createGraph(GraphProviderRequest request) {
        return null;
    }
}
