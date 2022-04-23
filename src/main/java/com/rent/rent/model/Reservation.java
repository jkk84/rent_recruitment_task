package com.rent.rent.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate start;

    private LocalDate end;

    @ManyToOne
    private Tenant tenant;

    @ManyToOne
    private ObjectForRent objectForRent;

    private BigDecimal cost;
}
