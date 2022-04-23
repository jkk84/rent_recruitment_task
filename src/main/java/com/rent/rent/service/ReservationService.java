package com.rent.rent.service;

import com.rent.rent.mapper.ReservationMapper;
import com.rent.rent.model.ObjectForRent;
import com.rent.rent.model.Reservation;
import com.rent.rent.model.Tenant;
import com.rent.rent.model.dto.ReservationDto;
import com.rent.rent.repository.ObjectForRentRepository;
import com.rent.rent.repository.ReservationRepository;
import com.rent.rent.repository.TenantRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    ObjectForRentRepository objectForRentRepository;

    @Autowired
    ReservationRepository reservationRepository;


    public ReservationDto createReservation(CreateReservation createReservation) {
        Tenant tenant = tenantRepository.findById(createReservation.getTenantId()).orElse(null);
        ObjectForRent objectForRent = objectForRentRepository.findById(createReservation.getObjectForRentId()).orElse(null);
        LocalDate start = createReservation.getStart();
        LocalDate end = createReservation.getEnd();

        if (tenant != null && objectForRent != null && start != null && end != null) {

            List<Reservation> reservationList = reservationRepository.findByObjectForRentAndBetweenDates(objectForRent, start, end);

            if (!reservationList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A reservation for " + objectForRent.getName() + " already exists at that time");
            } else {
                Reservation reservation = Reservation.builder()
                        .start(start)
                        .end(end)
                        .tenant(tenant)
                        .objectForRent(objectForRent)
                        .cost(objectForRent.getUnitPricePerDay().multiply(BigDecimal.valueOf(Period.between(start, end).getDays())))
                        .build();
                reservationRepository.save(reservation);
                return ReservationMapper.INSTANCE.reservationToDto(reservation);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data");
        }
    }

    @Data
    public static class CreateReservation {
        LocalDate start;
        LocalDate end;
        Long tenantId;
        Long objectForRentId;
    }
}
