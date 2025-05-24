package com.demo.aerztekasse.service;

import com.demo.aerztekasse.records.GroupedPlaceRecord;

public interface GroupPlaceService {

    public GroupedPlaceRecord getGroupedOpeningHoursByPlaceId(Long id);

}
