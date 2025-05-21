package com.demo.aerztekasse.service;

import com.demo.aerztekasse.records.PlaceDTORecord;

public interface GroupPlaceService {
	
    public PlaceDTORecord getGroupedOpeningHoursByPlaceId(Long id);
    
}
