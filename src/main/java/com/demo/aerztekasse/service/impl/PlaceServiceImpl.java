package com.demo.aerztekasse.service.impl;

import com.demo.aerztekasse.entity.DayOpening;
import com.demo.aerztekasse.entity.Place;
import com.demo.aerztekasse.records.OpenIntervalRecord;
import com.demo.aerztekasse.records.OpeningHoursRecord;
import com.demo.aerztekasse.records.PlaceRecord;
import com.demo.aerztekasse.repository.PlaceRepository;
import com.demo.aerztekasse.service.PlaceService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository repository;

    public PlaceServiceImpl(PlaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PlaceRecord> savePlace(List<PlaceRecord> places) {
        var entities = places.stream()
                .map(this::buildEntity)
                .collect(Collectors.toList());
        List<Place> saved = this.repository.saveAll(entities);
        return saved.stream()
                .map(this::buildRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceRecord> listAll() {
        return this.repository.findAll().stream()
                .map(this::buildRecord)
                .collect(Collectors.toList());
    }

    @Override
    public PlaceRecord findById(Long id) {
        var place = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Place not found: " + id));
        return buildRecord(place);
    }


    @Override
    public void deleteById(Long id) {
        if (!this.repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Place not found: " + id);
        }
        this.repository.deleteById(id);
    }

    @Override
    @Transactional
    public PlaceRecord updatePlace(PlaceRecord updatedPlace) {
        var existingPlace = this.repository.findById(updatedPlace.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));

        existingPlace.setLabel(updatedPlace.label());
        existingPlace.setLocation(updatedPlace.location());

        existingPlace.getDays().clear();

        var newDays = updatedPlace.openingHours().days().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(interval ->
                                DayOpening.builder()
                                        .dayOfWeek(DayOfWeek.valueOf(entry.getKey().toUpperCase()))
                                        .startTime(interval.start())
                                        .endTime(interval.end())
                                        .type(interval.type())
                                        .place(existingPlace)
                                        .build()))
                .toList();

        existingPlace.getDays().addAll(newDays);

        var savedPlace = this.repository.save(existingPlace);
        return buildRecord(savedPlace);
    }

    protected Place buildEntity(PlaceRecord record) {
        var place = Place.builder()
                .label(record.label())
                .location(record.location())
                .days(new ArrayList<>())
                .build();

        record.openingHours().days().forEach((day, intervals) -> {
            var dayOfWeek = DayOfWeek.valueOf(day.toUpperCase());
            intervals.forEach(intervalRecord -> {
                var dayOpening = DayOpening.builder()
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
                                ), Collectors.toList()
                        )
                ));
        return new PlaceRecord(place.getId(),
                place.getLabel(),
                place.getLocation(),
                new OpeningHoursRecord(dayMap));
    }

}