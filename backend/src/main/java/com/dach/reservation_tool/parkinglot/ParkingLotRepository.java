package com.dach.reservation_tool.parkinglot;

import com.dach.reservation_tool.conference.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, UUID> {

    @Query("SELECT t FROM Conference t WHERE t.bookerEmail=:mail")
    List<ParkingLot> findByMail(@Param("mail") String mail);


    @Query("SELECT t FROM Conference t WHERE YEAR(t.startTime) =:year AND DAY(t.startTime) =:dayOfMonth")
    List<ParkingLot> findAllByDate(@Param("year") int year,@Param("dayOfMonth") int dayOfMonth);

}
