package com.rent.rent.mapper;

import com.rent.rent.model.Reservation;
import com.rent.rent.model.dto.ReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    ReservationDto reservationToDto(Reservation reservation);
}
