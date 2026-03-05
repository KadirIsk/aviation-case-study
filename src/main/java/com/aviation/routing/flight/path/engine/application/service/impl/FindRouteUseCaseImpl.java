package com.aviation.routing.flight.path.engine.application.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.aviation.routing.flight.path.engine.application.service.FindRouteUseCase;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.Route;
import com.aviation.routing.flight.path.engine.domain.service.dto.GraphProviderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FindRouteUseCaseImpl implements FindRouteUseCase {
//    private final TransitGraphProvider<Location> transitGraphProvider;
//    private final RouteFacade routeFacade;

    @Override
    public List<Route> findRoute(Long originId, Long destinationId, LocalDate date) {
        GraphProviderRequest graphRequest = GraphProviderRequest.builder().date(date).build();
//        Graph<Location> transitGraph = transitGraphProvider.createGraph(graphRequest);
        // todo: origin'den baslayip destination'a ulasilan kriter gozetmeksizin tum route'lari bul
        //  ardindan business kurallarini isletip, gecersiz olanlari filtrele
        //  filtrelenmis route'lari don
        return Collections.emptyList();
    }
}
