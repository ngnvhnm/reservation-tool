package com.dach.reservation_tool.conference.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** This record is for query the events in specific time range
 *
 * @param startTime
 * @param endTime
 */
public record TimeRangeDto(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {
}
