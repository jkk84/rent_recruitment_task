package com.rent.rent.repository;

import com.rent.rent.model.ObjectForRent;
import com.rent.rent.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

//    @Query("select r from Reservation r " +
//            "left join r.objectForRent objectForRent " +
//            "where objectForRent = ?1 " +
//            "and r.start <= ?3 and r.end >= ?2")
//    List<Reservation> findByObjectForRentAndBetweenDates(ObjectForRent objectForRent, LocalDate start, LocalDate end);

    @Query("select case when count(r) > 0 then true else false end from Reservation r " +
            "left join r.objectForRent objectForRent " +
            "where objectForRent = ?1 " +
            "and r.start <= ?3 and r.end >= ?2")
    boolean existsByObjectForRentAndBetweenDates(ObjectForRent objectForRent, LocalDate start, LocalDate end);

    @Query("select case when count(r) > 0 then true else false end from Reservation r " +
            "left join r.objectForRent objectForRent " +
            "where r.id <> ?4 " +
            "and objectForRent = ?1 " +
            "and r.start <= ?3 and r.end >= ?2")
    boolean existsByObjectForRentAndBetweenDatesAndNotEqualId(ObjectForRent objectForRent, LocalDate start, LocalDate end, Long reservationId);
}
