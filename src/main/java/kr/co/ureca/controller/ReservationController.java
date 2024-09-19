package kr.co.ureca.controller;

import kr.co.ureca.dto.ReservationDeleteDto;
import kr.co.ureca.dto.ReservationSeatsDto;
import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<String> reserveSeat(@RequestBody Map<String, Long> request) {
        Long seatNo = request.get("seatNo");
        System.out.println(seatNo);
        if(seatNo == null){
            return ResponseEntity.badRequest().body("Seat number is missing");
        }
        boolean success = reservationService.selectSeatToUser(seatNo);
        if(success){
            return ResponseEntity.ok().body("Reservation successful");
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Seat is already reservation or occupied");
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

}
