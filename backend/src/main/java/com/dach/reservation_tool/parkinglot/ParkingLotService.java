package com.dach.reservation_tool.parkinglot;


import com.dach.reservation_tool.parkinglot.dto.ParkinglotCreateDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotResponseDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParkingLotService {
    private final ParkingLotRepository repository;
    private final ParkingLotMapper mapper;

    public ParkingLotService(ParkingLotRepository repository, ParkingLotMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ParkinglotResponseDto> getAllReservations() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public Optional<ParkinglotResponseDto> getParkinglotById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO);
    }

    public ParkinglotResponseDto createReservation(ParkinglotCreateDto createDto) {
        ParkingLot parkingLot = mapper.toEntity(createDto);
        ParkingLot savedReservation = repository.save(parkingLot);
        return mapper.toResponseDTO(savedReservation);
    }

    public ParkinglotResponseDto updateReservation(UUID id, ParkinglotUpdateDto updateDto) {
        return repository.findById(id)
                .map(existingReservation -> {
                    ParkingLot updatedReservation = mapper.updateEntity(existingReservation, updateDto);
                    ParkingLot savedReservation = repository.save(updatedReservation);
                    return mapper.toResponseDTO(savedReservation);
                })
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));
    }

    public void deleteReservation(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Reservation not found with ID: " + id);
        }
    }


    public ResponseEntity<String> recreateLastReservation(String mail, LocalDateTime date, TIMESLOT timeslot) {
        List<ParkingLot> allReservationsByMail = repository.findByMail(mail);
        if(allReservationsByMail.isEmpty()){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Von dem User: " + mail + "wurde noch keine Reservierung angelegt");
        }
        ParkingLot lastReservation = this.findLatest(allReservationsByMail);
        int startHour = 0;
        int endHour = switch (timeslot) {
            case NINE_TO_ONE -> {
                startHour = 9;
                yield 13;
            }
            case ONE_TO_FIVE -> {
                startHour = 13;
                yield 17;
            }
            case WHOLE_DAY -> {
                startHour = 9;
                yield 17;
            }
        };
        ParkinglotCreateDto newReservationDto = new ParkinglotCreateDto(mapTimeslotToTime(date, timeslot, startHour),
                mapTimeslotToTime(date, timeslot,endHour),
                lastReservation.getBookerEmail(),
                lastReservation.getParkingLotNumber());
        if(!this.doesCollide(mapper.toEntity(newReservationDto))) {
            repository.save(mapper.toEntity(newReservationDto));
            return  ResponseEntity.ok("Das Event wurde erfolgreich erstellt");
        }

        else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Zu dem gewünschten Zeitpunkt existiert bereits eine Reservierung");

        }


    }

    private LocalDateTime mapTimeslotToTime(LocalDateTime date, TIMESLOT timeslot, int hour){
        return LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), hour, 0);
    }



    //Simple sorting algorithm ...  function is used by recreateLastReservation
    private ParkingLot findLatest(List<ParkingLot> reservationList) {


        if(reservationList.size() > 1){
            List<ParkingLot> consideredList = new ArrayList<>(reservationList.stream().toList());
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
    public Boolean doesCollide(ParkingLot parkingLot){

        List<ParkingLot> parkinglotList = repository.findAll();
        for (ParkingLot value : parkinglotList) {
            if (value.getDate().getYear() == parkingLot.getDate().getYear()
                && value.getDate().getMonth() == parkingLot.getDate().getMonth()
                && value.getDate().getDayOfMonth() == parkingLot.getDate().getDayOfMonth()
                && (value.getTimeslot() == parkingLot.getTimeslot() || value.getTimeslot() == TIMESLOT.WHOLE_DAY || parkingLot.getTimeslot() == TIMESLOT.WHOLE_DAY)){

                return true;
            }
        }

        return false;
    }

}
