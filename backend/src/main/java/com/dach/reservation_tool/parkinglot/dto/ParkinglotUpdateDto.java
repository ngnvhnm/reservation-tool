package com.dach.reservation_tool.parkinglot.dto;

import java.time.LocalDateTime;

public record ParkinglotUpdateDto(
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
