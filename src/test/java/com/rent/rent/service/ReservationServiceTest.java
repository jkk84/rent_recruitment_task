package com.rent.rent.service;

import com.rent.rent.commons.exceptions.BadRequestException;
import com.rent.rent.mapper.MapStructMapperImpl;
import com.rent.rent.model.ObjectForRent;
import com.rent.rent.model.Reservation;
import com.rent.rent.model.Tenant;
import com.rent.rent.model.dto.ReservationDto;
import com.rent.rent.repository.ObjectForRentRepository;
import com.rent.rent.repository.ReservationRepository;
import com.rent.rent.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    final long tenantId = 1L;
    final long objectForRentId = 2L;

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ObjectForRentRepository objectForRentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MapStructMapperImpl mapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReservationWhenAllParametersAreNonNull() {
        Tenant tenant = mock(Tenant.class);
        ObjectForRent objectForRent = mock(ObjectForRent.class);
        ReservationDto reservationDto = mock(ReservationDto.class);
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        ReservationService.CreateReservation createReservation = mock(ReservationService.CreateReservation.class);

        Reservation reservation = Reservation.builder()
                .start(start)
                .end(end)
                .tenant(tenant)
                .objectForRent(objectForRent)
                .cost(BigDecimal.TEN)
                .build();

        when(createReservation.getTenantId()).thenReturn(tenantId);
        when(createReservation.getObjectForRentId()).thenReturn(objectForRentId);
        when(createReservation.getStart()).thenReturn(start);
        when(createReservation.getEnd()).thenReturn(end);
        when(tenantRepository.findById(createReservation.getTenantId())).thenReturn(Optional.of(tenant));
        when(objectForRentRepository.findById(createReservation.getObjectForRentId())).thenReturn(Optional.of(objectForRent));
        when(objectForRent.getUnitPricePerDay()).thenReturn(BigDecimal.TEN);
        when(mapper.reservationToReservationDto(reservation)).thenReturn(reservationDto);

        ReservationDto result = reservationService.createReservation(createReservation);
        verify(reservationRepository).save(reservation);
        assertEquals(result, reservationDto);
    }

    @Test
    void whenTenantFindThenReturnAllReservations() {
        Tenant tenant = mock(Tenant.class);
        Reservation reservation = mock(Reservation.class);
        ReservationDto reservationDto = mock(ReservationDto.class);

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.ofNullable(tenant));
        when(reservationRepository.getAllByTenantOrderByStartAsc(tenant)).thenReturn(Collections.singletonList(reservation));
        when(mapper.reservationsToReservationDtos(Collections.singletonList(reservation))).thenReturn(Collections.singletonList(reservationDto));

        List<ReservationDto> result = reservationService.getAllByTenant(tenantId);
        assertEquals(1, result.size());
        assertEquals(reservationDto, result.get(0));
    }

    @Test
    void whenTenantNotFindThenThrowBadRequestException() {
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> reservationService.getAllByTenant(tenantId));
        assertEquals(new BadRequestException().getMessage(), exception.getMessage());
    }

    @Test
    void calcReservationCost() {
        BigDecimal unitPricePerDay = BigDecimal.TEN;
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);

        BigDecimal result = reservationService.calcReservationCost(unitPricePerDay, start, end);
        assertEquals(BigDecimal.valueOf(20), result);
    }
}