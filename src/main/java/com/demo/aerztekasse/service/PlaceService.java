package com.demo.aerztekasse.service;

import java.util.List;

import com.demo.aerztekasse.records.PlaceRecord;

public interface PlaceService {

	public List<PlaceRecord> savePlace(List<PlaceRecord> places);
	public List<PlaceRecord> listAll();
	public PlaceRecord findById(Long id);
	public void deleteById(Long id);
    
}