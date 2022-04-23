package com.rent.rent.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class ObjectForRent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private BigDecimal unitPrice;

    private Double surface;

    private String description;

    @ManyToOne
    private Landlord landlord;

    @OneToMany(mappedBy = "objectForRent")
    private List<Reservation> reservationList;
}
