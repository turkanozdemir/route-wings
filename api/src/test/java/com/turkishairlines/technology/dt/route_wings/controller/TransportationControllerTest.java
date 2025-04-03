package com.turkishairlines.technology.dt.route_wings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.model.transportation.TransportationType;
import com.turkishairlines.technology.dt.route_wings.service.TransportationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransportationController.class)
public class TransportationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private TransportationService transportationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /transportations -> should return list")
    void getAllTransportations_ShouldReturnList() throws Exception {
        TransportationResponseDTO dto = TransportationResponseDTO.builder()
                .id(1L)
                .originName("IST")
                .destinationName("ANK")
                .transportationType(TransportationType.FLIGHT)
                .operatingDays(Set.of(1, 2, 3))
                .build();

        when(transportationService.getAllTransportations()).thenReturn(List.of(dto));

        mockMvc.perform(get("/transportations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].originName", is("IST")));
    }

    @Test
    @DisplayName("POST /transportations -> should create new transportation")
    void createTransportation_ShouldReturnCreated() throws Exception {
        TransportationRequestDTO requestDTO = TransportationRequestDTO.builder()
                .originId(1L)
                .destinationId(2L)
                .transportationType(TransportationType.BUS)
                .operatingDays(Set.of(1, 3, 5))
                .build();

        TransportationResponseDTO responseDTO = TransportationResponseDTO.builder()
                .id(100L)
                .originName("IST")
                .destinationName("IZM")
                .transportationType(TransportationType.BUS)
                .operatingDays(Set.of(1, 3, 5))
                .build();

        when(transportationService.saveTransportation(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/transportations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.originName", is("IST")));
    }

    @Test
    @DisplayName("GET /transportations/{id}/availability -> should return true/false")
    void isAvailableOnDate_ShouldReturnBoolean() throws Exception {
        when(transportationService.isTransportationAvailableOnDate(eq(1L), eq(LocalDate.of(2025, 4, 3))))
                .thenReturn(true);

        mockMvc.perform(get("/transportations/1/availability")
                        .param("date", "2025-04-03"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
