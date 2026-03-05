package com.aviation.routing.flight.path.engine.infrastructure.persistence;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.Graph;
import com.aviation.routing.flight.path.engine.domain.service.TransitGraphProvider;
import com.aviation.routing.flight.path.engine.domain.service.dto.GraphProviderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultimodalRuleBasedGraphProvider implements TransitGraphProvider<Location> {
    private static final int PAGE_SIZE = 500;
    private final TransportationService transportationService;

    @Override
    public Graph<Location> createGraph(GraphProviderRequest request) {
        Graph<Location> graph = new Graph<>();
        int value = getDayOfWeekValue(request);
        Page<Transportation> page;
        int pageNum = 0;
        do {
            page = transportationService.findByOperatingDay(value, PageRequest.of(pageNum++, PAGE_SIZE));
            process(page.getContent(), graph);
        }
        while (page.hasNext());
        return graph;
    }

    private void process(List<Transportation> transportations, Graph<Location> graph) {
        // todo: graph'i location'larla doldur
        //  her bir transportation icin
        //  originLocation graph'da var mi bak? eger varsa destinationLocation'i ve transportationType'i connection'lara add yap
        //  eger yoksa originLocation'i ilk defa destinationLocation'i ve transportationType'i connection'a koy ve listeye add yap
    }

    private static int getDayOfWeekValue(GraphProviderRequest request) {
        LocalDate searchDate = request.date();
        DayOfWeek dayOfWeek = searchDate.getDayOfWeek();
        return dayOfWeek.getValue();
    }
}
