package com.demo.aerztekasse.service;

import java.util.List;

import com.demo.aerztekasse.record.PlaceDTORecord;
import com.demo.aerztekasse.record.PlaceRecord;

public interface PlaceService {

    public List<PlaceRecord> savePlace(List<PlaceRecord> places);
    public List<PlaceRecord> listAll();
    public PlaceRecord findById(Integer id);
    public PlaceDTORecord getGroupedOpeningHoursByPlaceId(Integer id);
    public void deleteById(Integer id);

}
