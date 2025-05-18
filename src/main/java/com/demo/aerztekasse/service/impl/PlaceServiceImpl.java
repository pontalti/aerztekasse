package com.demo.aerztekasse.service.impl;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.demo.aerztekasse.entity.DayOpening;
import com.demo.aerztekasse.entity.OpenInterval;
import com.demo.aerztekasse.entity.OpeningHours;
import com.demo.aerztekasse.entity.Place;
import com.demo.aerztekasse.record.OpenIntervalRecord;
import com.demo.aerztekasse.record.OpeningGroupDTORecord;
import com.demo.aerztekasse.record.OpeningHoursRecord;
import com.demo.aerztekasse.record.PlaceDTORecord;
import com.demo.aerztekasse.record.PlaceRecord;
import com.demo.aerztekasse.repository.PlaceRepository;
import com.demo.aerztekasse.service.PlaceService;

@Service
public class PlaceServiceImpl implements PlaceService {
    
    private static final List<DayOfWeek> DAY_ORDER = List.of(DayOfWeek.MONDAY,
                                                                DayOfWeek.TUESDAY,
                                                                DayOfWeek.WEDNESDAY,
                                                                DayOfWeek.THURSDAY,
                                                                DayOfWeek.FRIDAY,
                                                                DayOfWeek.SATURDAY,
                                                                DayOfWeek.SUNDAY);
        
    private final PlaceRepository placeRepository;
    
    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public List<PlaceRecord> savePlace(List<PlaceRecord> places) {
        List<PlaceRecord> savedPlaces = new ArrayList<>();
        for (PlaceRecord record : places) {
            var place = Place.builder()
                    .label(record.label())
                    .location(record.location())
                    .build();

            var openingHours = OpeningHours.builder().build();
            List<DayOpening> dayOpenings = new ArrayList<>();

            if (record.openingHours() != null && record.openingHours().days() != null) {
                for (Map.Entry<String, List<OpenIntervalRecord>> entry : record.openingHours().days().entrySet()) {
                    String dayStr = entry.getKey();
                    List<OpenIntervalRecord> intervalsRecord = entry.getValue();

                    DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayStr.toUpperCase());

                    var dayOpening = DayOpening.builder()
                            .dayOfWeek(dayOfWeek)
                            .openingHours(openingHours)
                            .build();

                    var intervals = intervalsRecord.stream()
                            .map(i -> OpenInterval.builder()
                                    .startTime(i.start())
                                    .endTime(i.end())
                                    .type(i.type())
                                    .dayOpening(dayOpening)
                                    .build())
                            .toList();

                    dayOpening.setIntervals(intervals);
                    dayOpenings.add(dayOpening);
                }
            }

            openingHours.setDays(dayOpenings);
            place.setOpeningHours(openingHours);
            var saved = placeRepository.save(place);
            var placeRecord = buildPlaceRecord(saved);
            savedPlaces.add(placeRecord);
        }
        return savedPlaces;
    }

    @Override
    public List<PlaceRecord> listAll() {
        return placeRepository.findAll().stream()
                .map(this::buildPlaceRecord)
                .toList();
    }

    @Override
    public PlaceRecord findById(Integer id) {
        var place = placeRepository.findById(Long.valueOf(id)).orElseThrow();
        return buildPlaceRecord(place);
    }

    @Override
    public void deleteById(Integer id) {
        this.placeRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public PlaceDTORecord getGroupedOpeningHoursByPlaceId(Integer id) {
        var place = placeRepository.findById(Long.valueOf(id)).orElseThrow();

        Map<String, List<DayOfWeek>> grouped = groupDaysWithSameHours(place);

        var presentDays = grouped.values().stream()
                .flatMap(List::stream)
                .distinct()
                .toList();

        var missingDays = DAY_ORDER.stream()
                .filter(day -> !presentDays.contains(day))
                .toList();

        if (!missingDays.isEmpty()) {
            grouped.put("Closed", new ArrayList<>(missingDays));
        }

        var groups = grouped.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> DAY_ORDER.indexOf(entry.getValue().get(0))))
                .map(entry -> {
                    if ("Closed".equals(entry.getKey())) {
                        return new OpeningGroupDTORecord(formatDays(entry.getValue()), List.of("Closed"));
                    }

                    List<DayOfWeek> days = entry.getValue();
                    DayOfWeek referenceDay = days.get(0);
                    var opening = place.getOpeningHours().getDays().stream()
                            .filter(d -> d.getDayOfWeek().equals(referenceDay))
                            .findFirst()
                            .orElse(null);

                    List<String> intervals = new ArrayList<>();
                    if (opening != null) {
                        intervals = opening.getIntervals().stream()
                                .sorted(Comparator.comparing(OpenInterval::getStartTime))
                                .map(i -> i.getStartTime() + " - " + i.getEndTime())
                                .toList();
                    }

                    return new OpeningGroupDTORecord(formatDays(days), intervals);
                })
                .toList();
        
        return new PlaceDTORecord(place.getLabel(), place.getLocation(), groups);
    }

    private Map<String, List<DayOfWeek>> groupDaysWithSameHours(Place place) {
        Map<String, List<DayOfWeek>> grouped = new LinkedHashMap<>();

        for (DayOpening day : place.getOpeningHours().getDays()) {
            var intervals = day.getIntervals().stream()
                    .sorted(Comparator.comparing(OpenInterval::getStartTime))
                    .map(i -> i.getStartTime() + " - " + i.getEndTime())
                    .collect(Collectors.joining(", "));

            var dayOfWeek = day.getDayOfWeek();
            grouped.computeIfAbsent(intervals, k -> new ArrayList<>()).add(dayOfWeek);
        }

        grouped.forEach((k, v) -> v.sort(Comparator.comparingInt(DAY_ORDER::indexOf)));

        return grouped;
    }

    private String formatDays(List<DayOfWeek> days) {
        if (days.size() == 1) {
            return formatDay(days.get(0));
        }
        return formatDay(days.get(0)) + " - " + formatDay(days.get(days.size() - 1));
    }

    private String formatDay(DayOfWeek day) {
        var name = day.name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    protected PlaceRecord buildPlaceRecord(Place place) {
        Map<String, List<OpenIntervalRecord>> dayMap = new LinkedHashMap<>();

        for (DayOpening dayOpening : place.getOpeningHours().getDays()) {
            var dayKey = dayOpening.getDayOfWeek().name().toLowerCase();
            var intervals = dayOpening.getIntervals().stream()
                    .map(interval -> new OpenIntervalRecord(
                            interval.getStartTime(),
                            interval.getEndTime(),
                            interval.getType()
                    )).toList();
            dayMap.put(dayKey, intervals);
        }

        var openingHoursRecord = new OpeningHoursRecord(dayMap);
        return new PlaceRecord(place.getId(), place.getLabel(), place.getLocation(), openingHoursRecord);
    }

}