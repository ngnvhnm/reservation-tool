package com.dach.reservation_tool.conference;

import com.dach.reservation_tool.conference.dto.ConferenceCreateDto;
import com.dach.reservation_tool.conference.dto.ConferenceResponseDto;
import com.dach.reservation_tool.conference.dto.ConferenceUpdateDto;
import com.dach.reservation_tool.conference.dto.TimeRangeDto;
import net.fortuna.ical4j.util.RandomUidGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConferenceMapper {

    // Map ConferenceCreateDTO to Conference entity
    public Conference toEntity(ConferenceCreateDto dto) {
        var uid = new RandomUidGenerator().generateUid();
        Conference conference = new Conference();
        conference.setStartTime(dto.startTime());
        conference.setEndTime(dto.endTime());
        conference.setBookerEmail(dto.bookerEmail());
        conference.setConferenceType(dto.conferenceType());
        conference.setAttendeeList(dto.attendeeList());
        conference.setTimeOfBooking(LocalDateTime.now());// Automatically set time of booking
        conference.setCalendarId(uid);
        return conference;
    }

    // Map Conference entity to ConferenceResponseDTO
    public ConferenceResponseDto toResponseDTO(Conference entity) {
        return new ConferenceResponseDto(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getBookerEmail(),
                entity.getConferenceType(),
                entity.getTimeOfBooking(),
                entity.getAttendeeList()
        );
    }

    public TimeRangeDto toTimeRangeDTO(Conference entity) {
        return new TimeRangeDto(
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

    // Update Conference entity from ConferenceUpdateDTO
    public Conference updateEntity(Conference entity, ConferenceUpdateDto dto) {
        if (dto.startTime() != null) {
            entity.setStartTime(dto.startTime());
        }
        if (dto.endTime() != null) {
            entity.setEndTime(dto.endTime());
        }
        if (dto.attendeeList() != null) {
            entity.setAttendeeList(dto.attendeeList());
        }
        return entity;
    }
}
