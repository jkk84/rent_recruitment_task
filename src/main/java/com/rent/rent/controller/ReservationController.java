package com.rent.rent.controller;

import com.rent.rent.model.dto.ReservationDto;
import com.rent.rent.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationService.CreateReservation createReservation) {
        return ResponseEntity.ok(reservationService.createReservation(createReservation));
    }
}
