package com.turkishairlines.technology.dt.route_wings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationRequestDTO;
import com.turkishairlines.technology.dt.route_wings.model.location.LocationResponseDTO;
import com.turkishairlines.technology.dt.route_wings.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private LocationService locationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnAllLocations() throws Exception {
        List<LocationResponseDTO> locations = List.of(
                new LocationResponseDTO(1L, "Airport A", "Istanbul", "Turkey", "IST"),
                new LocationResponseDTO(2L, "Airport B", "Ankara", "Turkey", "ESB")
        );

        when(locationService.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Airport A"))
                .andExpect(jsonPath("$[1].locationCode").value("ESB"));
    }

    @Test
    void shouldReturnLocationById() throws Exception {
        LocationResponseDTO location = new LocationResponseDTO(1L, "Airport A", "Istanbul", "Turkey", "IST");

        when(locationService.getLocationById(1L)).thenReturn(location);

        mockMvc.perform(get("/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Airport A"))
                .andExpect(jsonPath("$.locationCode").value("IST"));
    }

    @Test
    void shouldReturnLocationByName() throws Exception {
        LocationResponseDTO location = new LocationResponseDTO(2L, "Airport B", "Ankara", "Turkey", "ESB");

        when(locationService.getLocationByName("Airport B")).thenReturn(location);

        mockMvc.perform(get("/locations").param("name", "Airport B"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void shouldCreateLocation() throws Exception {
        LocationRequestDTO request = new LocationRequestDTO("Airport C", "Izmir", "Turkey", "ADB");
        LocationResponseDTO response = new LocationResponseDTO(3L, "Airport C", "Izmir", "Turkey", "ADB");

        when(locationService.saveLocation(any(LocationRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Airport C"));
    }

    @Test
    void shouldUpdateLocation() throws Exception {
        LocationRequestDTO request = new LocationRequestDTO("Airport X", "CityX", "CountryX", "XXX");
        LocationResponseDTO response = new LocationResponseDTO(1L, "Airport X", "CityX", "CountryX", "XXX");

        when(locationService.updateLocation(eq(1L), any(LocationRequestDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Airport X"));
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        mockMvc.perform(delete("/locations/1"))
                .andExpect(status().isNoContent());

        verify(locationService).deleteLocationById(1L);
    }
}
