package com.demo.aerztekasse.record;

import java.util.List;

public record OpeningGroupDTORecord(
    String days, 
    List<String> hours) {}

