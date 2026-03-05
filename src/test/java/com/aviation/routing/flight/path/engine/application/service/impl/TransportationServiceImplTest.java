package com.aviation.routing.flight.path.engine.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepositoryPort;
import com.aviation.routing.flight.path.engine.domain.service.GraphStatePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransportationServiceImplTest {

    @Mock
    private TransportationRepositoryPort transportationRepositoryPort;

    @Mock
    private GraphStatePort graphStatePort;

    @InjectMocks
    private TransportationServiceImpl service;

    @Test
    void createTransportation_buildsTransportation_fromRequest_andSaves() {
        TransportationRequest request = TransportationRequest.builder()
            .originLocationId(10L)
            .destinationLocationId(20L)
            .transportationType("FLIGHT")
            .operatingDays((short)1)
            .build();

        Transportation savedFromRepo = Transportation.builder()
            .id(99L)
            .originLocationId(10L)
            .destinationLocationId(20L)
            .transportationType("FLIGHT")
            .operatingDays((short)1)
            .build();

        when(transportationRepositoryPort.save(any(Transportation.class))).thenReturn(savedFromRepo);

        Transportation result = service.createTransportation(request);

        ArgumentCaptor<Transportation> captor = ArgumentCaptor.forClass(Transportation.class);
        verify(transportationRepositoryPort).save(captor.capture());

        Transportation passed = captor.getValue();
        assertNull(passed.getId());
        assertEquals(10L, passed.getOriginLocationId());
        assertEquals(20L, passed.getDestinationLocationId());
        assertEquals("FLIGHT", passed.getTransportationType());
        assertEquals((short)1, passed.getOperatingDays());

        assertEquals(99L, result.getId());
        verify(graphStatePort).updateGraphAndBroadcast(savedFromRepo);
    }


    @Test
    void getTransportation_whenFound_returnsTransportation() {
        Transportation t = Transportation.builder().id(1L).originLocationId(1L).destinationLocationId(2L).build();
        when(transportationRepositoryPort.findById(1L)).thenReturn(Optional.of(t));

        Transportation result = service.getTransportation(1L);

        assertEquals(1L, result.getId());
        verify(transportationRepositoryPort).findById(1L);
    }

    @Test
    void getTransportation_whenNotFound_throwsRuntimeException() {
        when(transportationRepositoryPort.findById(404L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getTransportation(404L));
        assertEquals("Location not found", ex.getMessage());
        verify(transportationRepositoryPort).findById(404L);
    }

    @Test
    void deleteTransportation_delegatesToRepository() {
        Transportation t = Transportation.builder().id(5L).originLocationId(1L).destinationLocationId(2L).build();
        when(transportationRepositoryPort.findById(5L)).thenReturn(Optional.of(t));

        service.deleteTransportation(5L);

        verify(graphStatePort).deleteTransportationAndBroadcast(any(Transportation.class));
        verifyNoMoreInteractions(graphStatePort);
    }

    @Test
    void deleteTransportation_delegatesToGraphStatePort_whenTransportationExists() {
        Transportation existing = Transportation.builder()
            .id(5L)
            .originLocationId(1L)
            .destinationLocationId(2L)
            .transportationType("FLIGHT")
            .operatingDays((short)1)
            .build();

        when(transportationRepositoryPort.findById(5L)).thenReturn(Optional.of(existing));

        service.deleteTransportation(5L);

        verify(transportationRepositoryPort).findById(5L);
        verify(graphStatePort).deleteTransportationAndBroadcast(existing);
        verifyNoMoreInteractions(transportationRepositoryPort);
    }
}