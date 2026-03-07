package com.aviation.routing.flight.path.engine.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.CreateLocationUseCase;
import com.aviation.routing.flight.path.engine.application.dto.UpdateLocationUseCase;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.port.LocationPersistencePort;
import com.aviation.routing.flight.path.engine.domain.port.TransportationPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationPersistencePort locationPersistencePort;

    @Mock
    private TransportationPersistencePort transportationPersistencePort;

    @InjectMocks
    private LocationServiceImpl service;

    @Test
    void create_buildsDomainFromRequest_andSaves() {
        CreateLocationUseCase request = CreateLocationUseCase.builder()
            .name("Sabiha")
            .country("TR")
            .city("Istanbul")
            .locationCode("SAW")
            .build();

        Location savedFromRepo = Location.builder()
            .id(1L)
            .name("Sabiha")
            .country("TR")
            .city("Istanbul")
            .locationCode("SAW")
            .build();

        when(locationPersistencePort.save(any(Location.class))).thenReturn(savedFromRepo);

        Location result = service.create(request);

        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(locationPersistencePort).save(captor.capture());

        Location passed = captor.getValue();
        assertNull(passed.getId());
        assertEquals("Sabiha", passed.getName());
        assertEquals("TR", passed.getCountry());
        assertEquals("Istanbul", passed.getCity());
        assertEquals("SAW", passed.getLocationCode());

        assertEquals(1L, result.getId());
        assertEquals("Sabiha", result.getName());
    }

    @Test
    void getLocation_whenFound_returns() {
        Location loc = Location.builder().id(5L).name("A").country("TR").city("X").locationCode("AAA").build();
        when(locationPersistencePort.findById(5L)).thenReturn(Optional.of(loc));

        Location result = service.get(5L);

        assertEquals(5L, result.getId());
        verify(locationPersistencePort).findById(5L);
    }

    @Test
    void get_whenNotFound_throwsRuntimeException() {
        when(locationPersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.get(99L));
        verify(locationPersistencePort).findById(99L);
    }

    @Test
    void update_updatesFields_andSaves() {
        Long id = 5L;
        Location existing = Location.builder()
            .id(id)
            .name("OldName")
            .country("OldCountry")
            .city("OldCity")
            .locationCode("CODE")
            .build();

        UpdateLocationUseCase request = UpdateLocationUseCase.builder()
            .id(id)
            .name("NewName")
            .country("NewCountry")
            .city("NewCity")
            .build();

        when(locationPersistencePort.findById(id)).thenReturn(Optional.of(existing));
        when(locationPersistencePort.save(any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Location result = service.update(request);

        verify(locationPersistencePort).findById(id);
        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(locationPersistencePort).save(captor.capture());

        Location saved = captor.getValue();
        assertEquals("NewName", saved.getName());
        assertEquals("NewCountry", saved.getCountry());
        assertEquals("NewCity", saved.getCity());
        assertEquals("CODE", saved.getLocationCode());

        assertEquals("NewName", result.getName());
    }

    @Test
    void delete_delegatesToRepository() {
        service.delete(7L);

        verify(locationPersistencePort).deleteById(7L);
        verify(transportationPersistencePort).deleteByLocationId(7L);
        verifyNoMoreInteractions(locationPersistencePort, transportationPersistencePort);
    }
}