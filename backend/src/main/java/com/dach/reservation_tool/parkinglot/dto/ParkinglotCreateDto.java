package com.dach.reservation_tool.parkinglot.dto;

import com.dach.reservation_tool.parkinglot.PARKINGLOT_NUMBER;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ParkinglotCreateDto(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @Email String bookerEmail,
        @NotNull PARKINGLOT_NUMBER parkinglotNumber
) {
}
