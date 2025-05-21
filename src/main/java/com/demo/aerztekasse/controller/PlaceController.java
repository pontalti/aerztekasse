package com.demo.aerztekasse.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.aerztekasse.records.PlaceDTORecord;
import com.demo.aerztekasse.records.PlaceRecord;
import com.demo.aerztekasse.service.GroupPlaceService;
import com.demo.aerztekasse.service.PlaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Tag(name = "Places", description = "Endpoints to manage places and fetch grouped opening hours")
@RestController
public class PlaceController {

    private final PlaceService placeService;
    private final GroupPlaceService groupPlaceService;

    public PlaceController(PlaceService placeService, GroupPlaceService groupPlaceService) {
        this.placeService = placeService;
        this.groupPlaceService = groupPlaceService;
    }

    @Operation(summary = "Home", description = "Aerztekasse API home page")
    @ApiResponse(responseCode = "200", description = "Welcome message")
    @GetMapping(path = "/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Aerztekasse - code challenge - Home!");
    }

    @Operation(
        summary = "Create multiple places",
        description = "Saves a list of validated places",
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Places created successfully",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = PlaceRecord.class))
                )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid payload or empty list")
        }
    )
    @PostMapping(path = "/places", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaceRecord>> savePlaces(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "List of PlaceRecord to be created",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(implementation = PlaceRecord.class))
            )
        )
        @RequestBody
        @Valid @Size(min = 1, message = "Provide at least one location.")
        List<PlaceRecord> places
    ) {
        var saved = this.placeService.savePlace(places);
        return ResponseEntity.ok(saved);
    }

    @Operation(
        summary = "List all places",
        responses = @ApiResponse(responseCode = "200", description = "Returns list of places")
    )
    @GetMapping(path = "/places", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaceRecord>> listAll() {
        var list = this.placeService.listAll();
        return ResponseEntity.ok(list);
    }

    @Operation(
        summary = "Get place by ID",
        parameters = {
            @Parameter(name = "id", description = "Place ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Place found",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlaceRecord.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Place not found")
        }
    )
    @GetMapping(path = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceRecord> findById(
        @PathVariable("id") @NotNull Long id
    ) {
        var place = this.placeService.findById(id);
        return ResponseEntity.ok(place);
    }

    @Operation(
        summary = "Get grouped opening hours by place ID",
        parameters = @Parameter(name = "id", description = "Place ID", example = "1", required = true),
        responses = @ApiResponse(
            responseCode = "200", description = "Returns grouped opening hours",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PlaceDTORecord.class)
            )
        )
    )
    @GetMapping(path = "/places/{id}/opening-hours/grouped", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDTORecord> getGroupedOpeningHoursByPlaceId(
        @PathVariable("id") @NotNull Long id
    ) {
        var dto = this.groupPlaceService.getGroupedOpeningHoursByPlaceId(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "Delete place by ID",
        parameters = @Parameter(name = "id", description = "Place ID", example = "1", required = true),
        responses = {
            @ApiResponse(responseCode = "200", description = "Place deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Place not found")
        }
    )
    @DeleteMapping(path = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteById(
        @PathVariable("id") @NotNull Long id
    ) {
        this.placeService.deleteById(id);
        return ResponseEntity.ok("Place deleted successfully");
    }
}