package com.dach.reservation_tool.conference.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record ConferenceUpdateDto(
        LocalDateTime startTime,
        LocalDateTime endTime,
        @Nullable
        String attendeeList
) {
}
