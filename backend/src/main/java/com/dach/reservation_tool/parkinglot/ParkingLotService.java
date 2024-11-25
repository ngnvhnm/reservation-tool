package com.dach.reservation_tool.parkinglot;


import com.dach.reservation_tool.parkinglot.dto.ParkinglotCreateDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotResponseDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotTimeRangeDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotUpdateDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    public List<ParkinglotTimeRangeDto> getAllParkinglotsByDate(LocalDateTime date) {
        int year = date.getYear();
        int dayOfMonth =  date.getDayOfMonth();
        return repository.findAllByDate(year,dayOfMonth).stream()
                .map(mapper::toParkinglotTimeRangeDTO)
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
}
