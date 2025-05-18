package com.demo.aerztekasse.record;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceRecord(

    Long id,

    @NotBlank(message = "Label is mandatory")
    String label,

    @NotBlank(message = "Location is mandatory")
    String location,

    @Valid
    @NotNull(message = "OpeningHours is mandatory")
    @JsonProperty("openingHours")
    @JsonAlias({ "opening_hours", "openingHours" })
    OpeningHoursRecord openingHours
) {}