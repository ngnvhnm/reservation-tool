package com.dach.reservation_tool.parkinglot;



import com.dach.reservation_tool.parkinglot.dto.ParkinglotCreateDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotResponseDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotUpdateDto;
import net.fortuna.ical4j.util.RandomUidGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParkingLotMapper {

    // Map ParkinglotCreateDTO to ParkingLot entity
    public ParkingLot toEntity(ParkinglotCreateDto dto) {
        var uid = new RandomUidGenerator().generateUid();
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setDate(dto.startTime());
        switch(dto.startTime().getHour()){
            case 9:
                if(dto.endTime().getHour() == 13){
                    parkingLot.setTimeslot(TIMESLOT.NINE_TO_ONE);
                    break;
                } else if (dto.endTime().getHour() == 17) {
                    parkingLot.setTimeslot(TIMESLOT.WHOLE_DAY);
                    break;
                }
            case 13:
                parkingLot.setTimeslot(TIMESLOT.ONE_TO_FIVE);
        }
        parkingLot.setTimeOfBooking(LocalDateTime.now()); // Automatically set time of booking
        parkingLot.setBookerEmail(dto.bookerEmail());
        parkingLot.setParkingLotNumber(dto.parkinglotNumber());
        parkingLot.setCalendarId(uid);
        return parkingLot;
    }

    // Map Conference entity to ConferenceResponseDTO
    public ParkinglotResponseDto toResponseDTO(ParkingLot entity){
        int startHour = 0;
        int endHour = switch (entity.getTimeslot()) {
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
        return new ParkinglotResponseDto(
                entity.getId(),
                mapTime(entity, startHour),
                mapTime(entity, endHour),
                entity.getTimeOfBooking(),
                entity.getBookerEmail(),
                entity.getParkingLotNumber()
        );


    }

    public ParkingLot updateEntity(ParkingLot entity, ParkinglotUpdateDto dto) {
        if (dto.startTime() != null) {
            entity.setStartTime(dto.startTime());
        }
        if (dto.endTime() != null) {
            entity.setEndTime(dto.endTime());
        }
        return entity;
    }

    private LocalDateTime mapTime(ParkingLot entity, int hour){
        return LocalDateTime.of(entity.getDate().getYear(), entity.getDate().getMonthValue(), entity.getDate().getDayOfMonth(), hour, 0);
    }

}
