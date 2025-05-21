package com.demo.aerztekasse.service.impl;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.demo.aerztekasse.entity.DayOpening;
import com.demo.aerztekasse.entity.Place;
import com.demo.aerztekasse.records.OpenIntervalRecord;
import com.demo.aerztekasse.records.OpeningHoursRecord;
import com.demo.aerztekasse.records.PlaceRecord;
import com.demo.aerztekasse.repository.PlaceRepository;
import com.demo.aerztekasse.service.PlaceService;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository repository;

    public PlaceServiceImpl(PlaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PlaceRecord> savePlace(List<PlaceRecord> places) {
        List<Place> entities = places.stream()
            .map(this::buildEntity)
            .collect(Collectors.toList());
        List<Place> saved = repository.saveAll(entities);
        return saved.stream()
            .map(this::buildRecord)
            .collect(Collectors.toList());
    }

    @Override
    public List<PlaceRecord> listAll() {
        return repository.findAll().stream()
            .map(this::buildRecord)
            .collect(Collectors.toList());
    }

    @Override
    public PlaceRecord findById(Long id) {
        Place place = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Place not found: " + id));
        return buildRecord(place);
    }


    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Place not found: " + id);
        }
        repository.deleteById(id);
    }

    protected Place buildEntity(PlaceRecord record) {
        var place = Place.builder()
				            .label(record.label())
				            .location(record.location())
				            .days(new ArrayList<>())
				            .build();

        record.openingHours().days().forEach((day, intervals) -> {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(day.toUpperCase());
            intervals.forEach(intervalRecord -> {
                DayOpening dayOpening = DayOpening.builder()
								                    .dayOfWeek(dayOfWeek)
								                    .startTime(intervalRecord.start())
								                    .endTime(intervalRecord.end())
								                    .type(intervalRecord.type())
								                    .place(place)
								                    .build();
                place.getDays().add(dayOpening);
            });
        });
        return place;
    }

    protected PlaceRecord buildRecord(Place place) {
        var dayMap = place.getDays()
        					.stream()
        					.collect(Collectors.groupingBy(d -> d.getDayOfWeek().name().toLowerCase(),
						                LinkedHashMap::new,
						                Collectors.mapping(d -> new OpenIntervalRecord(d.getStartTime(),
																                        d.getEndTime(),
																                        d.getType()
						                					),Collectors.toList()
                )
            ));

        var openingHoursRecord = new OpeningHoursRecord(dayMap);
        return new PlaceRecord(place.getId(),
					            place.getLabel(),
					            place.getLocation(),
					            openingHoursRecord);
    }
    
}