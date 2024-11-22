package com.dach.reservation_tool.parkinglot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fortuna.ical4j.model.property.Uid;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "parkinglots")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "time_of_booking")
    private LocalDateTime timeOfBooking;

    @Column(name = "booker_email")
    private String bookerEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "parking_lot_number")
    private PARKINGLOT_NUMBER parkingLotNumber;

    // For easier finding in one calendar
    private Uid calendarId;
}
