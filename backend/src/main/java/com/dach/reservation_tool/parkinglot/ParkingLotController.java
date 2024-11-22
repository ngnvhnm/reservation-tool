package com.dach.reservation_tool.parkinglot;


import com.dach.reservation_tool.parkinglot.dto.ParkinglotCreateDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parkinglot")
public class ParkingLotController {

    private final ParkingLotService service;

    public ParkingLotController(ParkingLotService service) {
        this.service = service;
    }

    // 1. Get all conferences
    @GetMapping("/get-all-reservations")
    public List<ParkinglotResponseDto> getAllReservations() {
        return service.getAllReservations();
    }

    // 2. Get a conference by ID
    @GetMapping("/{id}")
    public ResponseEntity<ParkinglotResponseDto> getReservationById(@PathVariable UUID id) {
        return service.getParkinglotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Create a new conference
    @PostMapping("/create-reservation")
    public ResponseEntity<ParkinglotResponseDto> createReservation(@RequestBody @Valid ParkinglotCreateDto createDto) {
        ParkinglotResponseDto responseDto = service.createReservation(createDto);
        return ResponseEntity.ok(responseDto);
    }


    // 4. Delete a conference
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        try {
            service.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
