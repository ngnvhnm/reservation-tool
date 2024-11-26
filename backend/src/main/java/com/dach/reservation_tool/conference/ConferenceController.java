package com.dach.reservation_tool.conference;


import com.dach.reservation_tool.conference.dto.ConferenceCreateDto;
import com.dach.reservation_tool.conference.dto.ConferenceResponseDto;
import com.dach.reservation_tool.conference.dto.ConferenceUpdateDto;
import com.dach.reservation_tool.conference.dto.TimeRangeDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/v1/conferences")
public class ConferenceController {

    private final ConferenceService service;

    public ConferenceController(ConferenceService service) {
        this.service = service;
    }




    // 1. Get all conferences
    @GetMapping("/get-all-events")
    public List<ConferenceResponseDto> getAllConferences() {
        return service.getAllConferences();
    }

    // Get all conferences with a certain date:
    @GetMapping("/get-all-events-by-date")
    public List<TimeRangeDto> getAllConferencesByDate(@RequestBody LocalDateTime date) {
        return service.getAllConferencesByDate(date);
    }






    // 2. Get a conference by ID
    @GetMapping("/{id}")
    public ResponseEntity<ConferenceResponseDto> getConferenceById(@PathVariable UUID id) {
        return service.getConferenceById(id);
    }






    // 3. Create a new conference
    @PostMapping("/create-event")
    public ResponseEntity<String> createConference(@RequestBody @Valid ConferenceCreateDto createDto) {
        return service.createConference(createDto);
    }






    // 4. Update an existing conference
    @PutMapping("/{id}")
    public ResponseEntity<String> updateConference(
            @PathVariable UUID id,
            @RequestBody ConferenceUpdateDto updateDto) {
        try {
            return service.updateConference(id, updateDto);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }






    // 5. Delete a conference
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConference(@PathVariable UUID id) {
            try {
                return service.deleteConference(id);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.notFound().build();
            }
    }





    // 6. Searches for the last reservation that has been booked by a certain user(defined by an email adress)
    // and books the same reservation with a different startTime.
    @PostMapping("/{mail}/recreateLast")
    public ResponseEntity<String> recreateLastReservation(@PathVariable String mail,@RequestBody LocalDateTime startTime){
        return service.recreateLastReservation(mail,startTime);
    }


}
