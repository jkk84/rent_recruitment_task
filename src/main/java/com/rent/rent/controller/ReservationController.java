package com.rent.rent.controller;

import com.rent.rent.model.dto.ReservationDto;
import com.rent.rent.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationService.CreateReservation createReservation) {
        return ResponseEntity.ok(reservationService.createReservation(createReservation));
    }

    @PutMapping
    public ResponseEntity<ReservationDto> updateReservation(@RequestBody ReservationDto reservationDto) {
        return ResponseEntity.ok(reservationService.updateReservation(reservationDto));
    }
}
