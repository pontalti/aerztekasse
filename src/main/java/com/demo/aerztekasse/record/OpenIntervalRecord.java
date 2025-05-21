package com.demo.aerztekasse.record;

import com.demo.aerztekasse.annotation.ValidTime;

import jakarta.validation.constraints.NotBlank;

public record OpenIntervalRecord(

    @ValidTime(message = "Start time must be in format HH:mm")
    @NotBlank(message = "Start time is required")
    String start,

    @ValidTime(message = "Start time must be in format HH:mm")
    @NotBlank(message = "End time is required")
    String end,

    @NotBlank(message = "Type is required")
    String type
) {}