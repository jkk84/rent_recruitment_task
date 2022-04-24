package com.rent.rent.mapper;

import com.rent.rent.model.Reservation;
import com.rent.rent.model.dto.ReservationDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface MapStructMapper {

    ReservationDto reservationToReservationDto(Reservation reservation);

    List<ReservationDto> reservationsToReservationDtos(List<Reservation> reservation);
}
