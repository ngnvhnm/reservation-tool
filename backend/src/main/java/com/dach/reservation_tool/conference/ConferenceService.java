package com.dach.reservation_tool.conference;

import com.dach.reservation_tool.conference.dto.ConferenceCreateDto;
import com.dach.reservation_tool.conference.dto.ConferenceResponseDto;
import com.dach.reservation_tool.conference.dto.ConferenceUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ConferenceService {

    private final ConferenceRepository repository;
    private final ConferenceMapper mapper;








    public ConferenceService(ConferenceRepository repository, ConferenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }








    public List<ConferenceResponseDto> getAllConferences() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
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

        if (this.doesCollide(conference) == false) {
            Conference savedConference = repository.save(conference);
            return ResponseEntity.ok("Das Event wurde erfolgreich erstellt");
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Zu dem gewünschten Zeitpunkt existiert bereits eine Reservierung");  //Muss was besseres hin
        }
    }








    public ConferenceResponseDto updateConference(UUID id, ConferenceUpdateDto updateDto) {
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
        if(this.doesCollide(mapper.toEntity(newReservationDto)) == false) {
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

                                if (consideredList.get(i - 1).getEndTime().isBefore(consideredList.get(i).getEndTime())) {

                                } else {
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



        for (int i = 0;i < conferenceList.size();i++){
            if(   conferenceList.get(i).getEndTime().isAfter(conference.getStartTime())
               &&(conferenceList.get(i).getStartTime().isBefore(conference.getStartTime())
               || conferenceList.get(i).getStartTime().isEqual(conference.getStartTime()))
               && conferenceList.get(i).getConferenceType() == conference.getConferenceType()){

                return true;
            }

            else if( ( conferenceList.get(i).getEndTime().isAfter(conference.getEndTime())
                    || conferenceList.get(i).getEndTime().isEqual(conference.getEndTime()) )
                    && conferenceList.get(i).getStartTime().isBefore(conference.getEndTime())
                    && conferenceList.get(i).getConferenceType() == conference.getConferenceType()){

                return true;
            }


            else if( ( conferenceList.get(i).getEndTime().isBefore(conference.getEndTime())
                    || conferenceList.get(i).getEndTime().isEqual(conference.getEndTime()) )
                    &&(conferenceList.get(i).getStartTime().isAfter(conference.getStartTime())
                    || conferenceList.get(i).getStartTime().isEqual(conference.getStartTime()) )
                    && conferenceList.get(i).getConferenceType() == conference.getConferenceType()){

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
