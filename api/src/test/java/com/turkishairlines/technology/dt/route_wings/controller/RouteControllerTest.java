package com.turkishairlines.technology.dt.route_wings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkishairlines.technology.dt.route_wings.model.route.RouteDTO;
import com.turkishairlines.technology.dt.route_wings.service.RouteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
public class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private RouteService routeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnRoutesWhenValidRequest() throws Exception {
        RouteDTO sampleRoute = RouteDTO.builder().steps(List.of()).build();

        Mockito.when(routeService.findValidRoutes(eq("IST"), eq("SAW"), any(LocalDate.class)))
                .thenReturn(List.of(sampleRoute));

        mockMvc.perform(get("/routes")
                        .param("origin", "IST")
                        .param("destination", "SAW")
                        .param("date", "2025-04-06")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].steps").exists());

        Mockito.verify(routeService).findValidRoutes("IST", "SAW", LocalDate.of(2025, 4, 6));
    }

    @Test
    void shouldReturnBadRequestForInvalidDate() throws Exception {
        mockMvc.perform(get("/routes")
                        .param("origin", "IST")
                        .param("destination", "SAW")
                        .param("date", "not-a-date")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
