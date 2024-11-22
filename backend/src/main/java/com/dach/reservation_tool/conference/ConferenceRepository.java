package com.dach.reservation_tool.conference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, UUID> {

    @Query("SELECT t FROM Conference t WHERE t.bookerEmail=:mail")
    List<Conference> findByMail(@Param("mail") String mail);


//    @Query("SELECT t FROM Transaction t WHERE t.senderId =: id OR t.receiverId =:id")
//    List<Transaction> findRelatedTransactionsInDatabase(@Param("id") Integer id);



//    @Query("SELECT c FROM Conference c WHERE CAST(c.startTime as DATE) >= :startDate AND CAST(c.endTime as DATE) <= :endDate")
//    List<Conference> findConferencesByTimeRange(
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime
//    );
}
