package com.turkishairlines.technology.dt.route_wings.model.location;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String locationCode;
}
