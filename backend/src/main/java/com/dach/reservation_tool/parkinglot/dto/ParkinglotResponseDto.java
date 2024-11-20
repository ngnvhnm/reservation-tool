package com.dach.reservation_tool.parkinglot.dto;

import com.dach.reservation_tool.parkinglot.PARKINGLOT_NUMBER;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParkinglotResponseDto(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime timeOfBooking,
        String bookerEmail,
        PARKINGLOT_NUMBER parkinglotNumber
) {
}
