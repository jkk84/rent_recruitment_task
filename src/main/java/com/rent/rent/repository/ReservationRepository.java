package com.rent.rent.repository;

import com.rent.rent.model.ObjectForRent;
import com.rent.rent.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r " +
            "left join r.objectForRent objectForRent " +
            "where objectForRent = ?1 " +
//            "and (r.start between ?2 and ?3 or r.end between ?2 and ?3)")
            "and r.start < ?3 and r.end > ?2")
    List<Reservation> findByObjectForRentAndBetweenDates(ObjectForRent objectForRent, LocalDate start, LocalDate end);

    List<Reservation> findByObjectForRentId(Long objectForRentId);
}
