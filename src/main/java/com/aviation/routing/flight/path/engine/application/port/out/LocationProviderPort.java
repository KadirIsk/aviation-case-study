package com.aviation.routing.flight.path.engine.application.port.out;

import java.util.Map;
import java.util.Set;

public interface LocationProviderPort {
    Map<Long, String> getLocationNames(Set<Long> locationIds);
}