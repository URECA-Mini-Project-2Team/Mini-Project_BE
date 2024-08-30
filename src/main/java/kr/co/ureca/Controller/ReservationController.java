package kr.co.ureca.controller;

import kr.co.ureca.dto.ReservationRequest;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PutMapping("/ureca")
    public ResponseEntity<Seat> reservation(ReservationRequest reservationRequest){
        Seat reservatedSeat = reservationService.reserve(reservationRequest);
        return new ResponseEntity<>(reservatedSeat, HttpStatus.OK);
    }
}
