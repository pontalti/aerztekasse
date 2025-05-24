package com.demo.aerztekasse.service;

import com.demo.aerztekasse.records.PlaceRecord;

import java.util.List;

public interface PlaceService {

    public List<PlaceRecord> savePlace(List<PlaceRecord> places);

    public List<PlaceRecord> listAll();

    public PlaceRecord findById(Long id);

    public void deleteById(Long id);

    public PlaceRecord updatePlace(PlaceRecord updatedPlace);

}