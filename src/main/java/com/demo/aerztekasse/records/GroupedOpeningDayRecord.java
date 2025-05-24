package com.demo.aerztekasse.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GroupedOpeningDayRecord(

        @JsonProperty("day")
        String day,

        @JsonProperty("intervals")
        List<String> intervals) {
}