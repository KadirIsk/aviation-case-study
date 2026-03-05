package com.aviation.routing.flight.path.engine.domain.service;

import com.aviation.routing.flight.path.engine.domain.model.Transportation;

public interface GraphStatePort {
    void updateGraphAndBroadcast(Transportation transportation);
    void deleteTransportationAndBroadcast(Transportation transportation);
}