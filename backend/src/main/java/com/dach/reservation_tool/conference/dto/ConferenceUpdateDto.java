package com.dach.reservation_tool.conference.dto;

import java.time.LocalDateTime;

public record ConferenceUpdateDto(
        LocalDateTime startTime,
        LocalDateTime endTime,
        String attendeeList
) {
}
