package com.dach.reservation_tool.conference;

import com.dach.reservation_tool.conference.dto.ConferenceCreateDto;
import com.dach.reservation_tool.conference.dto.ConferenceResponseDto;
import com.dach.reservation_tool.conference.dto.ConferenceUpdateDto;
import com.dach.reservation_tool.conference.dto.TimeRangeDto;
import com.dach.reservation_tool.util.OneClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ConferenceService {

    private final ConferenceRepository repository;
    private final ConferenceMapper mapper;
    private final OneClient oneClient;








    public ConferenceService(ConferenceRepository repository, ConferenceMapper mapper, OneClient oneClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.oneClient = oneClient;
    }








    public List<ConferenceResponseDto> getAllConferences() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }


    public List<TimeRangeDto> getAllConferencesByDate(LocalDateTime date) {
        int year = date.getYear();
        int dayOfMonth =  date.getDayOfMonth();
        return repository.findAllByDate(year,dayOfMonth).stream()
                .map(mapper::toTimeRangeDTO)
                .toList();
    }








    public ResponseEntity<ConferenceResponseDto> getConferenceById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }







    public ResponseEntity<String> createConference(ConferenceCreateDto createDto) {
        Conference conference = mapper.toEntity(createDto);

        if (createDto.endTime().isBefore(createDto.startTime())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid time range");
        }

        if (!this.doesCollide(conference)) {
            oneClient.createEventForConference(createDto,
                    conference.getCalendarId(),
                    Arrays.stream(createDto.attendeeList().split(",")).toList(),
                    "Conference room booked by " + createDto.bookerEmail());
            repository.save(conference);
            // FIXME: This should be a proper JSON response
            String message = "{\"message\": \"Das Event wurde erfolgreich erstellt\"}";
            return ResponseEntity.ok(message);
        }
        else {
            // FIXME: This should be a proper JSON response
            String errorMessage = "{\"message\": \"Zu dem gewünschten Zeitpunkt existiert bereits eine Reservierung\"}";
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
        }
    }








    public ConferenceResponseDto updateConference(UUID id, ConferenceUpdateDto updateDto) {
        if (repository.existsById(id)) {
            var list = updateDto.attendeeList() == null ? null : updateDto.attendeeList();
            var oldConf = repository.findById(id);
            oneClient.editEventForConference(updateDto,
                    Collections.singletonList(list),
                    oldConf.orElseThrow().getConferenceType(),
                    oldConf.get().getCalendarId(),
                    oldConf.orElseThrow().getBookerEmail(),
                    "Conference room details updated by " + oldConf.orElseThrow().getBookerEmail());
        }
        return repository.findById(id)
                .map(existingConference -> {
                    Conference updatedConference = mapper.updateEntity(existingConference, updateDto);
                    Conference savedConference = repository.save(updatedConference);
                    return mapper.toResponseDTO(savedConference);
                })
                .orElseThrow(() -> new IllegalArgumentException("Conference not found with ID: " + id));
    }









    public void deleteConference(UUID id) {
        if (repository.existsById(id)) {
            var conference = repository.findById(id);
            oneClient.deleteEventForConference(conference.orElseThrow().getCalendarId(), conference.get().getConferenceType());
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Conference not found with ID: " + id);
        }
    }
    
    






    public ResponseEntity<String> recreateLastReservation(String mail, LocalDateTime startTime) {
        List<Conference> allReservationsByMail = repository.findByMail(mail);
        if(allReservationsByMail.isEmpty()){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Von dem User: " + mail + "wurde noch keine Reservierung angelegt");
        }
        Conference lastReservation = this.findLatest(allReservationsByMail);
        ConferenceCreateDto newReservationDto = new ConferenceCreateDto(startTime,
                                                                        startTime
                                                                                .plusHours(lastReservation.getEndTime().getHour()-lastReservation.getStartTime().getHour())
                                                                                .plusMinutes(lastReservation.getEndTime().getMinute()-lastReservation.getStartTime().getMinute())
                                                                                .plusSeconds(lastReservation.getEndTime().getSecond()-lastReservation.getStartTime().getSecond()),
                                                                        lastReservation.getBookerEmail(),
                                                                        lastReservation.getConferenceType(),
                                                                        lastReservation.getAttendeeList());
        if(!this.doesCollide(mapper.toEntity(newReservationDto))) {
            repository.save(mapper.toEntity(newReservationDto));
            return  ResponseEntity.ok("Das Event wurde erfolgreich erstellt");
          }

        else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Zu dem gewünschten Zeitpunkt existiert bereits eine Reservierung");

        }


    }








    //Simple sorting algorithm ...  function is used by recreateLastReservation
    private Conference findLatest(List<Conference> reservationList) {


               if(reservationList.size() > 1){
                            List<Conference> consideredList = new ArrayList<>(reservationList.stream().toList());
                            for (int i = 1; i < consideredList.size(); i++) {

                                if (!consideredList.get(i - 1).getTimeOfBooking().isBefore(consideredList.get(i).getTimeOfBooking())) {
                                    Collections.swap(consideredList, i - 1, i);
                                }


                            }

                            return consideredList.getLast();

                            }

               else{

                            return reservationList.getFirst();
               }

    }








    //Checks whether the event "conference" collides with some of the other events in the repository(Necessary to check before saving)
    public Boolean doesCollide(Conference conference){

        List<Conference> conferenceList = repository.findAll();


        for (Conference value : conferenceList) {
            if (value.getEndTime().isAfter(conference.getStartTime())
                    && (value.getStartTime().isBefore(conference.getStartTime())
                    || value.getStartTime().isEqual(conference.getStartTime()))
                    && value.getConferenceType() == conference.getConferenceType()) {

                return true;
            } else if ((value.getEndTime().isAfter(conference.getEndTime())
                    || value.getEndTime().isEqual(conference.getEndTime()))
                    && value.getStartTime().isBefore(conference.getEndTime())
                    && value.getConferenceType() == conference.getConferenceType()) {

                return true;
            } else if ((value.getEndTime().isBefore(conference.getEndTime())
                    || value.getEndTime().isEqual(conference.getEndTime()))
                    && (value.getStartTime().isAfter(conference.getStartTime())
                    || value.getStartTime().isEqual(conference.getStartTime()))
                    && value.getConferenceType() == conference.getConferenceType()) {

                return true;
            }


        }

        return false;
    }




//    public List<ConferenceResponseDto> getConferencesByTimeRange(TimeRangeDto timeRangeDto) {
//        List<Conference> conferences = repository.findConferencesByTimeRange(
//                timeRangeDto.startTime(),
//                timeRangeDto.endTime()
//        );
//        return conferences.stream()
//                .map(mapper::toResponseDTO)
//                .toList();
//    }
}
