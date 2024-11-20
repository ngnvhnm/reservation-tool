package com.dach.reservation_tool.conference.dto;

import com.dach.reservation_tool.conference.CONF_TYPE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ConferenceCreateDto(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @Email String bookerEmail,
        @NotNull CONF_TYPE conferenceType,
        String attendeeList
) {
}
