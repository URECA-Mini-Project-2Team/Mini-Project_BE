package kr.co.ureca.controller;

import kr.co.ureca.dto.ReservationDeleteDto;
import kr.co.ureca.dto.ReservationRequestDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping()
    public ResponseEntity<List<SeatDto>> getSeatList(){
        List<SeatDto> seats = reservationService.getAllSeats();
        return ResponseEntity.ok(seats);
    }
    @PatchMapping("/reservation")
    public ResponseEntity<String> reserveSeat(@RequestBody ReservationRequestDto reservationRequestDto) {
        boolean success = reservationService.selectSeatToUser(reservationRequestDto);

        if (success) {
            return new ResponseEntity<>("OK",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/delete")
    public ResponseEntity<String> deleteSeat(@RequestBody ReservationDeleteDto reservationDeleteDto) throws Exception {
        boolean success = reservationService.deleteSeat(reservationDeleteDto);

        if(success){
            return new ResponseEntity<>("OK",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/test")
//    public ResponseEntity<String> test() {
//        return new ResponseEntity<>("Test successful", HttpStatus.OK);
//    }

}
