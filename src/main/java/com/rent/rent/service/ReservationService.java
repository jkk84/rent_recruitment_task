package com.rent.rent.service;

import com.rent.rent.commons.exceptions.BadRequestException;
import com.rent.rent.commons.exceptions.ReservationConflictException;
import com.rent.rent.mapper.MapStructMapperImpl;
import com.rent.rent.model.ObjectForRent;
import com.rent.rent.model.Reservation;
import com.rent.rent.model.Tenant;
import com.rent.rent.model.dto.ReservationDto;
import com.rent.rent.repository.ObjectForRentRepository;
import com.rent.rent.repository.ReservationRepository;
import com.rent.rent.repository.TenantRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ReservationService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ObjectForRentRepository objectForRentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MapStructMapperImpl mapper;

    public ReservationDto createReservation(CreateReservation createReservation) {
        Tenant tenant = tenantRepository.findById(createReservation.getTenantId()).orElse(null);
        ObjectForRent objectForRent = objectForRentRepository.findById(createReservation.getObjectForRentId()).orElse(null);
        LocalDate start = createReservation.getStart();
        LocalDate end = createReservation.getEnd();

        if (Stream.of(tenant, start, end).anyMatch(Objects::nonNull) && objectForRent != null) {
            ifReservationAlreadyExistsThrowException(objectForRent, start, end, null);

            Reservation reservation = Reservation.builder()
                    .start(start)
                    .end(end)
                    .tenant(tenant)
                    .objectForRent(objectForRent)
                    .cost(calcReservationCost(objectForRent, start, end))
                    .build();

            reservationRepository.save(reservation);

            return mapper.reservationToReservationDto(reservation);
        } else {
            throw new BadRequestException();
        }
    }

    private BigDecimal calcReservationCost(ObjectForRent objectForRent, LocalDate start, LocalDate end) {
        return objectForRent.getUnitPricePerDay().multiply(BigDecimal.valueOf(Period.between(start, end).getDays()));
    }

    @Data
    public static class CreateReservation {
        LocalDate start;
        LocalDate end;
        Long tenantId;
        Long objectForRentId;
    }

    private void ifReservationAlreadyExistsThrowException(ObjectForRent objectForRent, LocalDate start, LocalDate end, Long reservationId) {
        if (reservationId == null ? reservationRepository.existsByObjectForRentAndBetweenDates(objectForRent, start, end) :
                reservationRepository.existsByObjectForRentAndBetweenDatesAndNotEqualId(objectForRent, start, end, reservationId)) {
            throw new ReservationConflictException(objectForRent.getName());
        }
    }

    public ReservationDto updateReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.findById(reservationDto.getId()).orElse(null);
        ObjectForRent objectForRent = objectForRentRepository.findById(reservationDto.getObjectForRent().getId()).orElse(null);
        Tenant tenant = tenantRepository.findById(reservationDto.getTenant().getId()).orElse(null);
        LocalDate start = reservationDto.getStart();
        LocalDate end = reservationDto.getEnd();

        if (reservation != null && objectForRent != null && tenant != null && start != null && end != null) {
            ifReservationAlreadyExistsThrowException(objectForRent, start, end, reservation.getId());

            reservation.setStart(start);
            reservation.setEnd(end);
            reservation.setTenant(tenant);
            reservation.setObjectForRent(objectForRent);
            reservation.setCost(calcReservationCost(objectForRent, start, end));

            reservationRepository.save(reservation);

            return mapper.reservationToReservationDto(reservation);
        } else {
            throw new BadRequestException();
        }
    }
}
