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

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransportationController.class)
class TransportationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportationService transportationService;

    @Test
    void create_returns201_andBody() throws Exception {
        Transportation saved = Transportation.builder()
            .id(11L)
            .originLocationId(10L)
            .destinationLocationId(20L)
            .transportationType("FLIGHT")
            .operatingDays("DAILY")
            .build();

        when(transportationService.createTransportation(any(TransportationRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/transportations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                  "originLocationId": 10,
                                  "destinationLocationId": 20,
                                  "transportationType": "FLIGHT",
                                  "operatingDays": "DAILY"
                                }
                                """))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(11))
            .andExpect(jsonPath("$.originLocationId").value(10))
            .andExpect(jsonPath("$.destinationLocationId").value(20))
            .andExpect(jsonPath("$.transportationType").value("FLIGHT"))
            .andExpect(jsonPath("$.operatingDays").value("DAILY"));

        verify(transportationService).createTransportation(any(TransportationRequest.class));
    }

    @Test
    void create_whenInvalid_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/transportations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                  "originLocationId": null,
                                  "destinationLocationId": 20,
                                  "transportationType": "FLIGHT",
                                  "operatingDays": "DAILY"
                                }
                                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getById_returns200_andBody() throws Exception {
        when(transportationService.getTransportation(3L)).thenReturn(
            Transportation.builder()
                .id(3L)
                .originLocationId(1L)
                .destinationLocationId(2L)
                .transportationType("BUS")
                .operatingDays("WEEKEND")
                .build()
        );

        mockMvc.perform(get("/api/v1/transportations/{id}", 3L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.transportationType").value("BUS"));

        verify(transportationService).getTransportation(3L);
    }

    @Test
    void getAll_returns200_andPage() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Transportation> page = new PageImpl<>(
            List.of(
                Transportation.builder().id(1L).originLocationId(10L).destinationLocationId(11L)
                    .transportationType("FLIGHT").operatingDays("DAILY").build(),
                Transportation.builder().id(2L).originLocationId(12L).destinationLocationId(13L)
                    .transportationType("BUS").operatingDays("WEEKEND").build()
            ),
            pageable,
            2
        );

        when(transportationService.getTransportations(any(TransportationRequest.class), any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(get("/api/v1/transportations")
                            .param("page", "0")
                            .param("size", "10")
                            .param("transportationType", "FLIGHT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].transportationType").value("FLIGHT"));

        verify(transportationService).getTransportations(any(TransportationRequest.class), any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void update_returns200_andBody() throws Exception {
        Transportation updated = Transportation.builder()
            .id(8L)
            .originLocationId(100L)
            .destinationLocationId(200L)
            .transportationType("TRAIN")
            .operatingDays("MON-FRI")
            .build();

        when(transportationService.updateTransportation(eq(8L), any(TransportationRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/transportations/{id}", 8L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                  "originLocationId": 100,
                                  "destinationLocationId": 200,
                                  "transportationType": "TRAIN",
                                  "operatingDays": "MON-FRI"
                                }
                                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(8))
            .andExpect(jsonPath("$.transportationType").value("TRAIN"));

        verify(transportationService).updateTransportation(eq(8L), any(TransportationRequest.class));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/transportations/{id}", 15L))
            .andExpect(status().isNoContent());

        verify(transportationService).deleteTransportation(15L);
    }
}