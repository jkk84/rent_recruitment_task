package com.rent.rent.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ObjectForRentDto {

    private Long id;

    private String name;

    private BigDecimal unitPricePerDay;

    private Double surface;

    private String description;

    private LandlordDto landlord;
}
