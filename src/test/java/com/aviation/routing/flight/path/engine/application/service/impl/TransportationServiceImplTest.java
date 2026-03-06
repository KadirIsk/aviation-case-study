package com.aviation.routing.flight.path.engine.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.aviation.routing.flight.path.engine.application.dto.CreateTransportationUseCase;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import com.aviation.routing.flight.path.engine.domain.port.TransportationPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransportationServiceImplTest {

    @Mock
    private TransportationPersistencePort transportationPersistencePort;

    @InjectMocks
    private TransportationServiceImpl service;

    @Test
    void createTransportation_builds_fromRequest_andSaves() {
        CreateTransportationUseCase request = CreateTransportationUseCase.builder()
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

        when(transportationPersistencePort.save(any(Transportation.class))).thenReturn(savedFromRepo);

        Transportation result = service.create(request);

        ArgumentCaptor<Transportation> captor = ArgumentCaptor.forClass(Transportation.class);
        verify(transportationPersistencePort).save(captor.capture());

        Transportation passed = captor.getValue();
        assertNull(passed.getId());
        assertEquals(10L, passed.getOriginLocationId());
        assertEquals(20L, passed.getDestinationLocationId());
        assertEquals("FLIGHT", passed.getTransportationType());
        assertEquals((short)1, passed.getOperatingDays());

        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals(10L, result.getOriginLocationId());
        assertEquals(20L, result.getDestinationLocationId());
    }

    @Test
    void getTransportation_whenFound_returns() {
        Transportation transportation = Transportation.builder()
            .id(1L)
            .originLocationId(1L)
            .destinationLocationId(2L)
            .transportationType("FLIGHT")
            .operatingDays((short)1)
            .build();

        when(transportationPersistencePort.get(1L)).thenReturn(transportation);

        Transportation result = service.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getOriginLocationId());
        assertEquals(2L, result.getDestinationLocationId());

        verify(transportationPersistencePort).get(1L);
    }

    @Test
    void get_whenNotFound_throwsRuntimeException() {
        when(transportationPersistencePort.get(404L))
            .thenThrow(new RuntimeException("Location not found"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.get(404L));

        assertEquals("Location not found", ex.getMessage());
        verify(transportationPersistencePort).get(404L);
    }

    @Test
    void delete_delegatesToPersistencePort() {
        service.delete(5L);

        verify(transportationPersistencePort).delete(5L);
        verifyNoMoreInteractions(transportationPersistencePort);
    }
}