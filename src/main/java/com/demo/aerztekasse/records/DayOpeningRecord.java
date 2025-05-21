package com.demo.aerztekasse.records;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record DayOpeningRecord(
    @JsonProperty("dayOfWeek")
    @JsonAlias({ "day_of_week", "dayOfWeek" })
    String dayOfWeek,
    
    @NotEmpty(message = "Interval list cannot be empty")
    @Valid 
    List<OpenIntervalRecord> intervals
) {}