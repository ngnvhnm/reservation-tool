package com.dach.reservation_tool.parkinglot;


import com.dach.reservation_tool.conference.dto.TimeRangeDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotCreateDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotResponseDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotTimeRangeDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parkinglots")
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

    // Get all parkinglots with a certain date:
    @GetMapping("/get-all-reservations-by-date")
    public List<ParkinglotTimeRangeDto> getAllParkinglotsByDate(@RequestBody LocalDateTime date) {
        return service.getAllParkinglotsByDate(date);
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

    // 4. Update an existing conference
    @PutMapping("/{id}")
    public ResponseEntity<ParkinglotResponseDto> updateReservation(
            @PathVariable UUID id,
            @RequestBody ParkinglotUpdateDto updateDto) {
        try {
            ParkinglotResponseDto responseDto = service.updateReservation(id, updateDto);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Delete a conference
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        try {
            service.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Searches for the last reservation that has been booked by a certain user(defined by an email adress)
    // and books the same reservation with a different startTime.
    @PostMapping("/{mail}/recreateLast")
    public ResponseEntity<String> recreateLastReservation(@PathVariable String mail,@RequestBody LocalDateTime date, @RequestBody TIMESLOT timeslot){
        return service.recreateLastReservation(mail,date, timeslot);
    }

}
