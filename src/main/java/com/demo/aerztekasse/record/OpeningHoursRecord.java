package com.demo.aerztekasse.record;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OpeningHoursRecord(
    @NotNull(message = "Days map cannot be null")
    @NotEmpty(message = "Days map cannot be empty")
    @JsonProperty("days")
    Map<String, @NotEmpty(message = "Interval list cannot be empty") @Valid List<@Valid OpenIntervalRecord>> days
) {}