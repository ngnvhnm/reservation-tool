package com.dach.reservation_tool.conference;


import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
import lombok.*;
import net.fortuna.ical4j.model.property.Uid;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bookerEmail;
    @Enumerated(EnumType.STRING)
    private CONF_TYPE conferenceType;
    private LocalDateTime timeOfBooking;
    private String attendeeList;
    // For easier finding in one calendar
    private Uid calendarId;
}
