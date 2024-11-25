package com.dach.reservation_tool.parkinglot.dto;

import com.dach.reservation_tool.parkinglot.TIMESLOT;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** This record is for query the events in specific time range
 *
 * @param timeslot
 */
public record ParkinglotTimeRangeDto(
        @NotNull TIMESLOT timeslot
        ) {
}
