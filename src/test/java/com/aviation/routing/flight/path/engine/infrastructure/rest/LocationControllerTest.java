package com.aviation.routing.flight.path.engine.infrastructure.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.application.service.LocationService;
import com.aviation.routing.flight.path.engine.domain.model.Location;
import com.aviation.routing.flight.path.engine.infrastructure.rest.controller.LocationController;
import com.aviation.routing.flight.path.engine.infrastructure.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(GlobalExceptionHandler.class)
@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Test
    void create_returns200_andStandardResponseBody() throws Exception {
        Location saved = Location.builder()
            .id(1L)
            .name("Sabiha")
            .country("TR")
            .city("Istanbul")
            .locationCode("SAW")
            .build();

        when(locationService.createLocation(any(LocationRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/locations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                         {
                                           "name": "Sabiha",
                                           "country": "TR",
                                           "city": "Istanbul",
                                           "locationCode": "SAW"
                                         }
                                         """))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Location created successfully"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.name").value("Sabiha"))
            .andExpect(jsonPath("$.data.country").value("TR"))
            .andExpect(jsonPath("$.data.city").value("Istanbul"))
            .andExpect(jsonPath("$.data.locationCode").value("SAW"));

        verify(locationService).createLocation(any(LocationRequest.class));
    }

    @Test
    void create_whenInvalid_returns400_andStandardErrorResponse() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                         {
                                           "name": "",
                                           "country": "TR",
                                           "city": "Istanbul",
                                           "locationCode": "SAW"
                                         }
                                         """))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getById_returns200_andStandardResponseBody() throws Exception {
        when(locationService.getLocation(5L)).thenReturn(
            Location.builder()
                .id(5L)
                .name("Ataturk")
                .country("TR")
                .city("Istanbul")
                .locationCode("IST")
                .build()
        );

        mockMvc.perform(get("/api/v1/locations/{id}", 5L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Location retrieved successfully"))
            .andExpect(jsonPath("$.data.id").value(5))
            .andExpect(jsonPath("$.data.locationCode").value("IST"));

        verify(locationService).getLocation(5L);
    }

    @Test
    void getById_whenServiceThrowsRuntimeException_returns404_andStandardErrorResponse() throws Exception {
        when(locationService.getLocation(404L)).thenThrow(new RuntimeException("Location not found"));

        mockMvc.perform(get("/api/v1/locations/{id}", 404L))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Location not found"));

        verify(locationService).getLocation(404L);
    }

    @Test
    void getAll_returns200_andPage() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Location> page = new PageImpl<>(
            List.of(
                Location.builder().id(1L).name("A").country("TR").city("X").locationCode("AAA").build(),
                Location.builder().id(2L).name("B").country("TR").city("Y").locationCode("BBB").build()
            ),
            pageable,
            2
        );

        when(locationService.getLocations(any(LocationRequest.class), any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(get("/api/v1/locations")
                            .param("page", "0")
                            .param("size", "10")
                            .param("name", "A"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].locationCode").value("AAA"));

        verify(locationService).getLocations(any(LocationRequest.class), any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void update_returns200_andStandardResponseBody() throws Exception {
        Location updated = Location.builder()
            .id(7L)
            .name("NewName")
            .country("TR")
            .city("Ankara")
            .locationCode("ESB")
            .build();

        when(locationService.updateLocation(eq(7L), any(LocationRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/locations/{id}", 7L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                         {
                                           "name": "NewName",
                                           "country": "TR",
                                           "city": "Ankara",
                                           "locationCode": "ESB"
                                         }
                                         """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Location updated successfully"))
            .andExpect(jsonPath("$.data.id").value(7))
            .andExpect(jsonPath("$.data.locationCode").value("ESB"));

        verify(locationService).updateLocation(eq(7L), any(LocationRequest.class));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/locations/{id}", 9L))
            .andExpect(status().isNoContent());

        verify(locationService).deleteLocation(9L);
    }
}