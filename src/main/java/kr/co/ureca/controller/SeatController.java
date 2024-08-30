package kr.co.ureca.controller;

import kr.co.ureca.dto.ReservationDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping()
    public ResponseEntity<List<SeatDto>> getSeatList() {
        List<SeatDto> SetList = seatService.getSeatList();
        return ResponseEntity.ok(SetList);
    }

    @PatchMapping("/reservation")
    public ResponseEntity reservationSeat(@RequestBody ReservationDto reservationDto) throws Exception {
        return seatService.reservationSeat(reservationDto);
    }
}
