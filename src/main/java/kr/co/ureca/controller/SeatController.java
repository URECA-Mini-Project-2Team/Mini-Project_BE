package kr.co.ureca.controller;

import kr.co.ureca.dto.SeatDto;
import kr.co.ureca.entity.User;
import kr.co.ureca.service.ReservationService;
import kr.co.ureca.service.SeatService;
import kr.co.ureca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatService seatService;
    private final UserService userService;
    private final ReservationService reservationService;

    @Autowired
    public SeatController(SeatService seatService, UserService userService, ReservationService reservationService) {
        this.seatService = seatService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping()
    public ResponseEntity<List<SeatDto.ResponseDto.SeatListDto>> getSeatList() {
        List<SeatDto.ResponseDto.SeatListDto> SetList = seatService.getSeatList();
        return ResponseEntity.ok(SetList);
    }

    @PatchMapping("/reservation")
    public ResponseEntity<Void> reservationSeat(@RequestBody SeatDto.RequestDto.ReservationDto reservationDto) throws Exception {
        reservationService.reservationSeat(reservationDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/delete")
    public ResponseEntity<Void> deleteSeat(@RequestBody SeatDto.RequestDto.DeleteDto deleteDto) throws Exception {
        User user = userService.checkUserExistOrNot(deleteDto);
        seatService.deleteSeat(user);
        userService.updateUserStatus(user);
        return ResponseEntity.ok().build();
    }
}
