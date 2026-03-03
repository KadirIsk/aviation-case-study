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
import com.aviation.routing.flight.path.engine.domain.repository.TransportationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransportationServiceImplTest {

    @Mock
    private TransportationRepository transportationRepository;

    @InjectMocks
    private TransportationServiceImpl service;

    @Test
    void createTransportation_buildsTransportation_fromRequest_andSaves() {
        TransportationRequest request = TransportationRequest.builder()
            .originLocationId(10L)
            .destinationLocationId(20L)
            .transportationType("FLIGHT")
            .operatingDays("DAILY")
            .build();

        Transportation savedFromRepo = Transportation.builder()
            .id(99L)
            .originLocationId(10L)
            .destinationLocationId(20L)
            .transportationType("FLIGHT")
            .operatingDays("DAILY")
            .build();

        when(transportationRepository.save(any(Transportation.class))).thenReturn(savedFromRepo);

        Transportation result = service.createTransportation(request);

        ArgumentCaptor<Transportation> captor = ArgumentCaptor.forClass(Transportation.class);
        verify(transportationRepository).save(captor.capture());

        Transportation passed = captor.getValue();
        assertNull(passed.getId());
        assertEquals(10L, passed.getOriginLocationId());
        assertEquals(20L, passed.getDestinationLocationId());
        assertEquals("FLIGHT", passed.getTransportationType());
        assertEquals("DAILY", passed.getOperatingDays());

        assertEquals(99L, result.getId());
    }

    @Test
    void getTransportation_whenFound_returnsTransportation() {
        Transportation t = Transportation.builder().id(1L).originLocationId(1L).destinationLocationId(2L).build();
        when(transportationRepository.findById(1L)).thenReturn(Optional.of(t));

        Transportation result = service.getTransportation(1L);

        assertEquals(1L, result.getId());
        verify(transportationRepository).findById(1L);
    }

    @Test
    void getTransportation_whenNotFound_throwsRuntimeException() {
        when(transportationRepository.findById(404L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getTransportation(404L));
        assertEquals("Location not found", ex.getMessage());
        verify(transportationRepository).findById(404L);
    }

    @Test
    void deleteTransportation_delegatesToRepository() {
        service.deleteTransportation(5L);

        verify(transportationRepository).deleteById(5L);
        verifyNoMoreInteractions(transportationRepository);
    }
}