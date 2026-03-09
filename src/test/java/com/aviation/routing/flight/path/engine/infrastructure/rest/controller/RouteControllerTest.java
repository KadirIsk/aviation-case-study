package com.aviation.routing.flight.path.engine.infrastructure.rest.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.aviation.routing.flight.path.engine.application.port.in.FindFlightRoutesUseCase;
import com.aviation.routing.flight.path.engine.infrastructure.rest.dto.RouteResponse;
import com.aviation.routing.flight.path.engine.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc(addFilters = false)
class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindFlightRoutesUseCase findFlightRoutesUseCase;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void findRoutes_shouldReturnSuccess() throws Exception {
        RouteResponse response = RouteResponse.builder()
            .route(RouteResponse.RouteDto.builder()
                       .title("via Istanbul")
                       .steps(List.of())
                       .build())
            .build();

        when(findFlightRoutesUseCase.execute(anyLong(), anyLong(), anyShort()))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/routes")
                            .param("originId", "1")
                            .param("destinationId", "2")
                            .param("operatingDays", "MONDAY,TUESDAY")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("success"))
            .andExpect(jsonPath("$.data[0].route.title").value("via Istanbul"));
    }
}
