package com.rent.rent.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectForRent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private BigDecimal unitPricePerDay;

    private Double surface;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private Landlord landlord;

    @OneToMany(mappedBy = "objectForRent")
    private List<Reservation> reservationList;
}
