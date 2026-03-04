package com.aviation.routing.flight.path.engine.domain.service;

import com.aviation.routing.flight.path.engine.domain.model.route.finder.Graph;
import com.aviation.routing.flight.path.engine.domain.service.dto.GraphProviderRequest;

public interface TransitGraphProvider<T> {
    Graph<T> createGraph(GraphProviderRequest request);
}