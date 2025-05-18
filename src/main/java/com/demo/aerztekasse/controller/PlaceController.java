package com.demo.aerztekasse.controller;

import java.util.List;

import com.demo.aerztekasse.record.PlaceRecord;
import com.demo.aerztekasse.service.PlaceService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.aerztekasse.record.PlaceDTORecord;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
public class PlaceController {

    private final PlaceService service;

    public PlaceController(PlaceService placeService) {
        this.service = placeService;
    }

    @GetMapping("/")
	public @ResponseBody ResponseEntity<String> home() {
        return ResponseEntity.ok("Aerztekasse - code challenge - Home!");
	}

    @PostMapping(path = "/places", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<List<PlaceRecord>> createPlace(@RequestBody @Valid List<PlaceRecord> places) {
        var savedPlace = this.service.savePlace(places);
        return ResponseEntity.ok(savedPlace);
    }

    @GetMapping(path = "/places", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<PlaceRecord>> listAll() {
        var places = this.service.listAll();
		return ResponseEntity.ok(places);
	}

    @GetMapping(path = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<PlaceRecord> findById(@PathVariable("id") @NotNull Integer id) {
        var place = this.service.findById(id);
		return ResponseEntity.ok(place);
	}

    @GetMapping(path = "/places/{id}/opening-hours/grouped", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<PlaceDTORecord> getGroupedOpeningHoursByPlaceId(@PathVariable("id") @NotNull Integer id) {
        var place = this.service.getGroupedOpeningHoursByPlaceId(id);
		return ResponseEntity.ok(place);
	}

    @DeleteMapping(path = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> deleteById(@PathVariable("id") @NotNull Integer id) {
        this.service.deleteById(id);
		return ResponseEntity.ok("Place deleted successfully");
	}

}