package com.demo.aerztekasse.service.impl;

import com.demo.aerztekasse.entity.DayOpening;
import com.demo.aerztekasse.records.OpeningGroupDTORecord;
import com.demo.aerztekasse.records.PlaceDTORecord;
import com.demo.aerztekasse.repository.PlaceRepository;
import com.demo.aerztekasse.service.GroupPlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupPlaceServiceImpl implements GroupPlaceService {

    private final List<DayOfWeek> dayOrder;
    private final PlaceRepository repository;

    public GroupPlaceServiceImpl(PlaceRepository repository, List<DayOfWeek> dayOrder) {
        this.repository = repository;
        this.dayOrder = dayOrder;
    }

    @Override
    public PlaceDTORecord getGroupedOpeningHoursByPlaceId(Long id) {
        var place = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found: " + id));

        var byDay = place.getDays().stream().collect(Collectors.groupingBy(DayOpening::getDayOfWeek));

        LinkedHashMap<String, List<DayOfWeek>> groupedDays = new LinkedHashMap<>();
        Map<String, List<String>> intervalMap = new HashMap<>();

        for (DayOfWeek day : this.dayOrder) {
            var opens = byDay.getOrDefault(day, Collections.emptyList());
            List<String> intervals;

            if (opens.isEmpty()) {
                intervals = List.of("closed");
            } else {
                intervals = opens.stream()
                        .sorted(Comparator.comparing(DayOpening::getStartTime))
                        .map(o -> o.getStartTime() + " - " + o.getEndTime())
                        .collect(Collectors.toList());
            }

            var key = String.join(", ", intervals);
            groupedDays.computeIfAbsent(key, k -> new ArrayList<>()).add(day);
            intervalMap.putIfAbsent(key, intervals);
        }

        var openingGroups = groupedDays.entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> this.dayOrder.indexOf(e.getValue().getFirst())))
                .map(e -> new OpeningGroupDTORecord(
                        formatDays(e.getValue()),
                        intervalMap.get(e.getKey())
                ))
                .collect(Collectors.toList());

        return new PlaceDTORecord(place.getId(),
                place.getLabel(),
                place.getLocation(),
                openingGroups);
    }

    protected String formatDay(DayOfWeek day) {
        var name = day.name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    protected String formatDays(List<DayOfWeek> days) {
        if (days.size() == 1) {
            return formatDay(days.getFirst());
        }
        return formatDay(days.getFirst()) + " - " + formatDay(days.getLast());
    }
}
