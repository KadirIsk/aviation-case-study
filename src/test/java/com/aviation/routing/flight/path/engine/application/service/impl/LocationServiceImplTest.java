package com.aviation.routing.flight.path.engine.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.domain.port.LocationPersistencePort;
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

    @InjectMocks
    private LocationServiceImpl service;

    @Test
    void createLocation_buildsDomainFromRequest_andSaves() {
        LocationRequest request = LocationRequest.builder()
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

        Location result = service.createLocation(request);

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
    void getLocation_whenFound_returnsLocation() {
        Location loc = Location.builder().id(5L).name("A").country("TR").city("X").locationCode("AAA").build();
        when(locationPersistencePort.findById(5L)).thenReturn(Optional.of(loc));

        Location result = service.getLocation(5L);

        assertEquals(5L, result.getId());
        verify(locationPersistencePort).findById(5L);
    }

    @Test
    void getLocation_whenNotFound_throwsRuntimeException() {
        when(locationPersistencePort.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getLocation(99L));
        assertEquals("Location not found", ex.getMessage());
        verify(locationPersistencePort).findById(99L);
    }

    @Test
    void deleteLocation_delegatesToRepository() {
        service.deleteLocation(7L);

        verify(locationPersistencePort).deleteById(7L);
        verifyNoMoreInteractions(locationPersistencePort);
    }
}