package com.aviation.routing.flight.path.engine.infrastructure.persistence;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.Graph;
import com.aviation.routing.flight.path.engine.domain.service.TransitGraphProvider;
import com.aviation.routing.flight.path.engine.domain.service.dto.GraphProviderRequest;
import org.springframework.stereotype.Component;

@Component
public class MultimodalRuleBasedGraphProvider implements TransitGraphProvider<Location> {

    @Override
    public Graph<Location> createGraph(GraphProviderRequest request) {
        int value = getDayOfWeekValue(request);

        return null;
    }

    private static int getDayOfWeekValue(GraphProviderRequest request) {
        LocalDate searchDate = request.date();
        DayOfWeek dayOfWeek = searchDate.getDayOfWeek();
        return dayOfWeek.getValue();
    }
}
