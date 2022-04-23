package com.rent.rent.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ReservationDto {

    private Long id;

    private LocalDate start;

    private LocalDate end;

    private TenantDto tenant;

    private ObjectForRentDto objectForRent;

    private BigDecimal cost;
}
