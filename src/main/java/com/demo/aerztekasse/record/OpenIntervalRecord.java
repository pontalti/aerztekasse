package com.demo.aerztekasse.record;

import jakarta.validation.constraints.NotBlank;

public record OpenIntervalRecord(

    @NotBlank(message = "Start time is required")
    String start,

    @NotBlank(message = "End time is required")
    String end,

    @NotBlank(message = "Type is required")
    String type
) {}