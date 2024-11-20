package com.dach.reservation_tool.conference.dto;

import com.dach.reservation_tool.conference.CONF_TYPE;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConferenceResponseDto(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String bookerEmail,
        CONF_TYPE conferenceType,
        LocalDateTime timeOfBooking,
        String attendeeList
) {
}
