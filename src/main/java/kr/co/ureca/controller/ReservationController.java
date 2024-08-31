package kr.co.ureca.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ureca.dto.DeleteReservationRequest;
import kr.co.ureca.dto.ReservationRequest;
import kr.co.ureca.entity.Seat;
import kr.co.ureca.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좌석 예약 API ")
@RestController
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @Operation(summary = "좌석 예약", responses = {
            @ApiResponse(responseCode = "200", description = "예약 성공"),
            @ApiResponse(responseCode = "400", description = "예약 실패")
    })
    @PatchMapping("/ureca")
    public ResponseEntity<Seat> reservation(ReservationRequest reservationRequest){
        Seat reservatedSeat = reservationService.reserve(reservationRequest);
        return new ResponseEntity<>(reservatedSeat, HttpStatus.OK);
    }

    @Operation(summary = "예약 취소", responses = {
            @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
            @ApiResponse(responseCode = "400", description = "예약 취소 실패")
    })
    @PatchMapping("/ureca/delete")
    public ResponseEntity<Seat> deleteReservation(DeleteReservationRequest deleteReservationRequest){
        Seat deletedSeatReservation = reservationService.deleteReservation(deleteReservationRequest);
        return new ResponseEntity<>(deletedSeatReservation, HttpStatus.OK);
    }
}
