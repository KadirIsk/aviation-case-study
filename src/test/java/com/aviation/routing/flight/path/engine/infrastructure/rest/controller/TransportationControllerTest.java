package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import static org.mockito.ArgumentMatchers.any;
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

import com.aviation.routing.flight.path.engine.application.dto.CreateTransportationUseCase;
import com.aviation.routing.flight.path.engine.application.dto.TransportationFilterRequest;
import com.aviation.routing.flight.path.engine.application.dto.UpdateTransportationUseCase;
import com.aviation.routing.flight.path.engine.application.service.TransportationService;
import com.aviation.routing.flight.path.engine.common.exception.GlobalExceptionHandler;
import com.aviation.routing.flight.path.engine.domain.model.Transportation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Import(GlobalExceptionHandler.class)
@WebMvcTest(controllers = TransportationController.class)
@ActiveProfiles("local")
@org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
class TransportationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportationService transportationService;

    @MockBean
    private com.aviation.routing.flight.path.engine.infrastructure.security.JwtService jwtService;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Test
    void create_returns200_andStandardResponseBody() throws Exception {
        Transportation saved = Transportation.builder()
            .id(11L)
            .originLocationId(10L)
            .destinationLocationId(20L)
            .transportationType("FLIGHT")
            .operatingDays((short) 1)
            .build();

        when(transportationService.create(any(CreateTransportationUseCase.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/transportations")
                            .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                         {
                                           "originLocationId": 10,
                                           "destinationLocationId": 20,
                                           "transportationType": "FLIGHT",
                                           "operatingDays": ["MONDAY"]
                                         }
                                         """))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Transportation created successfully"))
            .andExpect(jsonPath("$.data.id").value(11))
            .andExpect(jsonPath("$.data.transportationType").value("FLIGHT"))
            .andExpect(jsonPath("$.data.operatingDays[0]").value("MONDAY"));

        verify(transportationService).create(any(CreateTransportationUseCase.class));
    }

    @Test
    void create_whenInvalid_returns400_andStandardErrorResponse() throws Exception {
        mockMvc.perform(post("/api/v1/transportations")
                            .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                         {
                                           "originLocationId": -1,
                                           "destinationLocationId": 20,
                                           "transportationType": "FLIGHT",
                                           "operatingDays": ["MONDAY"]
                                         }
                                         """))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("SYS_VAL_001"))
            .andExpect(jsonPath("$.message").value("originLocationId: must be greater than 0"))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void getById_returns200_andStandardResponseBody() throws Exception {
        when(transportationService.get(3L)).thenReturn(
            Transportation.builder()
                .id(3L)
                .originLocationId(1L)
                .destinationLocationId(2L)
                .transportationType("BUS")
                .operatingDays((short) 1)
                .build()
        );

        mockMvc.perform(get("/api/v1/transportations/{id}", 3L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Transportation retrieved successfully"))
            .andExpect(jsonPath("$.data.id").value(3))
            .andExpect(jsonPath("$.data.transportationType").value("BUS"));

        verify(transportationService).get(3L);
    }

    @Test
    void getById_whenServiceThrowsRuntimeException_returns404_andStandardErrorResponse() throws Exception {
        when(transportationService.get(404L)).thenThrow(new com.aviation.routing.flight.path.engine.application.exception.ResourceNotFoundException(
            com.aviation.routing.flight.path.engine.common.exception.ErrorCode.TRN_NF_001, "Transportation not found"));

        mockMvc.perform(get("/api/v1/transportations/{id}", 404L))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("TRN_NF_001"))
            .andExpect(jsonPath("$.message").value("Transportation not found"));

        verify(transportationService).get(404L);
    }

    @Test
    void getAll_returns200_andPage() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Transportation> page = new PageImpl<>(
            List.of(
                Transportation.builder().id(1L).originLocationId(10L).destinationLocationId(11L)
                    .transportationType("FLIGHT").operatingDays((short) 1).build(),
                Transportation.builder().id(2L).originLocationId(12L).destinationLocationId(13L)
                    .transportationType("BUS").operatingDays((short) 1).build()
            ),
            pageable,
            2
        );

        when(transportationService.getTransportations(
            any(TransportationFilterRequest.class),
            any(org.springframework.data.domain.Pageable.class)
        ))
            .thenReturn(page);

        mockMvc.perform(get("/api/v1/transportations")
                            .param("page", "0")
                            .param("size", "10")
                            .param("transportationType", "FLIGHT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(2))
            .andExpect(jsonPath("$.data.content[0].id").value(1))
            .andExpect(jsonPath("$.data.content[0].transportationType").value("FLIGHT"));

        verify(transportationService).getTransportations(
            any(TransportationFilterRequest.class),
            any(org.springframework.data.domain.Pageable.class)
        );
    }

    @Test
    void update_returns200_andStandardResponseBody() throws Exception {
        Transportation updated = Transportation.builder()
            .id(8L)
            .originLocationId(100L)
            .destinationLocationId(200L)
            .transportationType("TRAIN")
            .operatingDays((short) 1)
            .build();

        when(transportationService.update(any(UpdateTransportationUseCase.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/transportations/{id}", 8L)
                            .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                         {
                                           "operatingDays": ["MONDAY"]
                                         }
                                         """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Transportation updated successfully"))
            .andExpect(jsonPath("$.data.id").value(8))
            .andExpect(jsonPath("$.data.transportationType").value("TRAIN"))
            .andExpect(jsonPath("$.data.operatingDays[0]").value("MONDAY"));

        verify(transportationService).update(any(UpdateTransportationUseCase.class));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/transportations/{id}", 15L)
                            .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isNoContent());

        verify(transportationService).delete(15L);
    }
}