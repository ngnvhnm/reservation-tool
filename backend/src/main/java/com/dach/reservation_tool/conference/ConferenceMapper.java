package com.dach.reservation_tool.conference;

import com.dach.reservation_tool.conference.dto.ConferenceCreateDto;
import com.dach.reservation_tool.conference.dto.ConferenceResponseDto;
import com.dach.reservation_tool.conference.dto.ConferenceUpdateDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConferenceMapper {

    // Map ConferenceCreateDTO to Conference entity
    public Conference toEntity(ConferenceCreateDto dto) {
        Conference conference = new Conference();
        conference.setStartTime(dto.startTime());
        conference.setEndTime(dto.endTime());
        conference.setBookerEmail(dto.bookerEmail());
        conference.setConferenceType(dto.conferenceType());
        conference.setAttendeeList(dto.attendeeList());
        conference.setTimeOfBooking(LocalDateTime.now()); // Automatically set time of booking
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
