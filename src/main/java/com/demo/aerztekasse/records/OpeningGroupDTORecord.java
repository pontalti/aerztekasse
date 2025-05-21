package com.demo.aerztekasse.records;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpeningGroupDTORecord(
		
		@JsonProperty("day") String day,

		@JsonProperty("intervals") List<String> intervals)
{}