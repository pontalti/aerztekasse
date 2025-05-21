package com.demo.aerztekasse.records;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlaceDTORecord(
    Long id,
    String label,
    String location,

    @JsonProperty("openingHours")
    @JsonAlias({"opening_hours"})
    List<OpeningGroupDTORecord> openingHours
) {}