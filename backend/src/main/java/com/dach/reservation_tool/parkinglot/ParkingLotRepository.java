package com.dach.reservation_tool.parkinglot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, UUID> {


    @Query("SELECT t FROM ParkingLot t WHERE YEAR(t.date) =:year AND DAY(t.date) =:dayOfMonth")
    List<ParkingLot> findAllByDate(@Param("year") int year, @Param("dayOfMonth") int dayOfMonth);
}
