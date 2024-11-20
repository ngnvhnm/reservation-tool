package com.dach.reservation_tool.parkinglot;



import com.dach.reservation_tool.parkinglot.dto.ParkinglotCreateDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotResponseDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotUpdateDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParkingLotMapper {

    // Map ParkinglotCreateDTO to ParkingLot entity
    public ParkingLot toEntity(ParkinglotCreateDto dto) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setStartTime(dto.startTime());
        parkingLot.setEndTime(dto.endTime());
        parkingLot.setTimeOfBooking(LocalDateTime.now()); // Automatically set time of booking
        parkingLot.setBookerEmail(dto.bookerEmail());
        parkingLot.setParkingLotNumber(dto.parkinglotNumber());
        return parkingLot;
    }

    // Map Conference entity to ConferenceResponseDTO
    public ParkinglotResponseDto toResponseDTO(ParkingLot entity) {
        return new ParkinglotResponseDto(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getTimeOfBooking(),
                entity.getBookerEmail(),
                entity.getParkingLotNumber()
        );
    }

    // Update Conference entity from ConferenceUpdateDTO
    public ParkingLot updateEntity(ParkingLot entity, ParkinglotUpdateDto dto) {
        if (dto.startTime() != null) {
            entity.setStartTime(dto.startTime());
        }
        if (dto.endTime() != null) {
            entity.setEndTime(dto.endTime());
        }
        return entity;
    }

}
