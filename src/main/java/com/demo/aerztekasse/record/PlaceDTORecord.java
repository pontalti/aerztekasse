package com.demo.aerztekasse.record;

import java.util.List;

public record PlaceDTORecord(
    String name,
    String address,
    List<OpeningGroupDTORecord> openingHours
) {}