package com.dach.reservation_tool.conference;


import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
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
}
