package com.rent.rent.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReservationConflictException extends ResponseStatusException {

    public ReservationConflictException(String objectForRentName) {
        super(HttpStatus.CONFLICT, "A reservation for " + objectForRentName + " already exists at that time.");
    }
}
